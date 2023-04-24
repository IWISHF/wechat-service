package com.merkle.wechat.service.follower;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.ExportLogDao;
import com.merkle.wechat.common.dao.aia.AIAUserInfoDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.ExportLog;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.aia.AIAUserInfo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.specification.FollowerCustomerSpecs;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TagService;
import com.merkle.wechat.service.TokenServiceImpl;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.follower.FollowerCheckVo;
import com.merkle.wechat.vo.follower.FollowerExportVo;
import com.merkle.wechat.vo.follower.FollowerRelatedVo;
import com.merkle.wechat.vo.follower.FollowerSearchVo;
import com.merkle.wechat.vo.follower.FollowerTagResult;
import com.merkle.wechat.vo.follower.FollowerVo;

import weixin.popular.api.UserAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.user.FollowResult;
import weixin.popular.bean.user.FollowResult.Data;
import weixin.popular.bean.user.User;
import weixin.popular.bean.user.UserInfoList;

@Component
public class FollowerServiceImpl implements FollowerService {
    protected Logger logger = LoggerFactory.getLogger("FollowerServiceImpl");
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired TokenServiceImpl tokenServiceImpl;
    private @Autowired TagService tagServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired ResourceLoader resourceLoader;
    private @Autowired ExportLogDao exportLogDaoImpl;
    private @Autowired AIAUserInfoDao aiaUserInfoDaoImpl;
    private @Autowired FollowerBindInfoService followerBindInfoServiceImpl;
    @Value("${export.path}")
    private String exportPath;

    @Override
    public void syncFollowersFromWechat(String appId) throws Exception {
        List<String> openids = new ArrayList<>();
        WechatPublicNo pbNo = pbNoServiceImpl.findOneByAuthorizerAppid(appId);
        Integer getCount = 0;
        Integer totalNo = 1;
        String nextOpenid = null;
        while (getCount < totalNo) {
            FollowResult result = UserAPI.userGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), nextOpenid);
            if (result.isSuccess()) {
                totalNo = result.getTotal();
                getCount += result.getCount();
                nextOpenid = result.getNext_openid();
                Data data = result.getData();
                if (data != null && data.getOpenid() != null && data.getOpenid().length != 0) {
                    List<String> ids = Arrays.asList(data.getOpenid());
                    if (ids != null) {
                        if (ids.contains(null)) {
                            List<String> nonNullIds = new ArrayList<>();
                            for (String id : ids) {
                                if (id != null) {
                                    nonNullIds.add(id);
                                }
                            }
                            openids.addAll(nonNullIds);
                        } else {
                            openids.addAll(ids);
                        }
                    }
                }
            } else {
                throw new ServiceWarn(result.getErrmsg(), Integer.valueOf(result.getErrcode()));
            }

        }
        Set<String> existsOpenids = followerDaoImpl.getDistinctOpenIdsByAppId(appId);
        openids.removeAll(existsOpenids);
        existsOpenids.clear();
        int size = openids.size();
        int syncCount = 0;
        int segmentGap = 30000;
        if (size > segmentGap) {
            int start = 0;
            int end = segmentGap - 1;
            while (syncCount < size) {
                final int totalNofinal = totalNo;
                final int startFinal = start;
                final int endFinal = end;
                AsyncUtil.asyncRun(() -> {
                    syncFollower(startFinal, endFinal, totalNofinal, openids, pbNo, appId);
                });

                syncCount += end - start + 1;
                start = end + 1;
                end += segmentGap;
                if (end > size) {
                    end = size - 1;
                }
            }
        } else {
            syncFollower(totalNo, openids, pbNo, appId);
        }

    }

    private void syncFollower(int start, int end, Integer totalNo, List<String> openids, WechatPublicNo pbNo,
            String appId) {
        int size = end - start + 1;
        int from = 0;
        int to = 0;
        int syncCount = 0;
        if (size >= 100) {
            to = 99;
        } else {
            to = size - 1;
        }
        while (syncCount < size) {
            logger.info(
                    "===== sync follower total:" + totalNo + " from:" + start + from + " to:" + start + to + " ====");
            List<Follower> followers = new ArrayList<>();
            UserInfoList userInfoList = UserAPI.userInfoBatchget(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId),
                    "zh_CN", openids.subList(start + from, start + to + 1), 5);
            if (userInfoList.isSuccess()) {
                for (User user : userInfoList.getUser_info_list()) {
                    boolean isFollowerExist = followerDaoImpl.existsByOpenid(user.getOpenid());
                    if (isFollowerExist) {
                        continue;
                    }
                    Follower follower = new Follower();
                    convertUserToFollower(user, follower, pbNo);
                    follower.setPubNoAppId(appId);
                    follower.setRecordToLoyaltySuccess(false);
                    followers.add(follower);
                }
                followerDaoImpl.save(followers);
                syncCount += to - from + 1;
                from = to + 1;
                to += 100;
                if (to > size) {
                    to = size - 1;
                }
            } else {
                logger.info("===== sync follower Retry:" + totalNo + " from:" + start + from + " to:" + start + to
                        + " ====");
                // throw new ServiceWarn(userInfoList.getErrmsg(),
                // Integer.valueOf(userInfoList.getErrcode()));
            }
        }
    }

    public void syncFollower(Integer totalNo, List<String> openids, WechatPublicNo pbNo, String appId) {
        int size = openids.size();
        int from = 0;
        int to = 0;
        int syncCount = 0;
        if (size >= 100) {
            to = 99;
        } else {
            to = size - 1;
        }
        while (syncCount < size) {
            logger.info("===== sync follower total:" + totalNo + " from:" + from + " to:" + to + " ====");
            List<Follower> followers = new ArrayList<>();
            UserInfoList userInfoList = UserAPI.userInfoBatchget(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId),
                    "zh_CN", openids.subList(from, to + 1), 5);
            if (userInfoList.isSuccess()) {
                for (User user : userInfoList.getUser_info_list()) {
                    boolean isFollowerExist = followerDaoImpl.existsByOpenid(user.getOpenid());
                    if (isFollowerExist) {
                        continue;
                    }
                    Follower follower = new Follower();
                    convertUserToFollower(user, follower, pbNo);
                    follower.setPubNoAppId(appId);
                    follower.setRecordToLoyaltySuccess(false);
                    followers.add(follower);
                }
                followerDaoImpl.save(followers);
                syncCount += to - from + 1;
                from = to + 1;
                to += 100;
                if (to > size) {
                    to = size - 1;
                }
            } else {
                logger.info("===== sync follower Retry:" + totalNo + " from:" + from + " to:" + to + " ====");
            }
        }
    }

    @Override
    public String getFollowerUnionId(String openid) {
        Follower follower = followerDaoImpl.findOneByOpenid(openid);
        return StringUtils.isEmpty(follower.getUnionid()) ? follower.getOpenid() : follower.getUnionid();
    }

    @Override
    public Follower findOrCreateFollower(String openid, String appId) throws Exception {
        Follower follower = followerDaoImpl.findOneByOpenid(openid);
        if (follower == null) {
            return createFollower(openid, appId);
        }

        return follower;
    }

    @Override
    public Follower syncLatestFollowerInfo(Follower follower) throws Exception {
        return fillInFollowerInfo(follower);
    }

    private Follower fillInFollowerInfo(Follower follower) throws Exception {
        String accessToken = tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId());
        WechatPublicNo pbNo = pbNoServiceImpl.findOneByAuthorizerAppid(follower.getPubNoAppId());
        User userInfo = UserAPI.userInfo(accessToken, follower.getOpenid(), 5);
        if (userInfo.isSuccess()) {
            convertUserToFollower(userInfo, follower, pbNo);
            follower.setPubNoAppId(follower.getPubNoAppId());
            // follower.setRecordToLoyaltySuccess(false);
            follower = followerDaoImpl.save(follower);
            return follower;
        }
        return null;
    }

    @Override
    public Follower createFollower(String openid, String appId) throws Exception {
        Follower follower = new Follower();
        follower.setOpenid(openid);
        follower.setPubNoAppId(appId);
        return fillInFollowerInfo(follower);
    }

    @Override
    public Follower updateFollower(Follower follower) throws Exception {
        return followerDaoImpl.save(follower);
    }

    @Override
    public Follower unsubscribeFollower(String openid) throws Exception {
        Follower follower = followerDaoImpl.findOneByOpenid(openid);
        if (follower == null) {
            return null;
        }
        follower.setSubscribe(0);
        follower.getTags().clear();
        return updateFollower(follower);
    }

    private void convertUserToFollower(User userInfo, Follower follower, WechatPublicNo pbNo) {
        BeanUtils.copyProperties(userInfo, follower, "nickname", "nicknameEmoji");
        if (userInfo.getNickname() == null) {
            userInfo.setNickname("未知");
            userInfo.setNickname_emoji("未知");
        }
        follower.setNickname(Base64Utils.encodeToString(userInfo.getNickname().getBytes()));
        follower.setNicknameEmoji(Base64Utils.encodeToString(userInfo.getNickname_emoji().getBytes()));
        follower.setSubscribeTime(userInfo.getSubscribe_time());
        follower.setSubscribeScene(userInfo.getSubscribe_scene());
        follower.setTagidListStr(JSONUtil.objectJsonStr(userInfo.getTagid_list()));
        Object[] tagidList = follower.getTagidList();
        if (tagidList != null && tagidList.length > 0) {
            for (Object o : tagidList) {
                try {
                    Tag tag = tagServiceImpl.getTagByWeixinTagId(Integer.valueOf(o.toString()), pbNo.getId());
                    follower.getTags().add(tag);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        follower.setQrScene(userInfo.getQr_scene());
        follower.setQrSceneStr(userInfo.getQr_scene_str());
        follower.setPrivilegeStr(JSONUtil.objectJsonStr(userInfo.getPrivilege()));
    }

    @Override
    public Follower findOneByOpenid(String openid) {
        return followerDaoImpl.findOneByOpenid(openid);
    }

    @Override
    public Set<String> findDistinctOpenIdsByCondition(String appId, String province) {
        return followerDaoImpl.getDistinctOpenIdsByAppIdAndProvince(appId, province);
    }

    @Override
    public Follower getFollowerByAppIdAndOpenid(WechatPublicNo pbNo, String openid) {
        return followerDaoImpl.findOneByOpenidAndPubNoAppId(openid, pbNo.getAuthorizerAppid());
    }

    @Override
    public Pagination<FollowerVo> searchByNickname(WechatPublicNo pbNo, String nickname, Pageable pageable) {
        Page<Follower> page = followerDaoImpl.findByNicknameContainingAndPubNoAppId(nickname, pbNo.getAuthorizerAppid(),
                pageable);
        List<Follower> content = page.getContent();
        List<FollowerVo> vos = convertFollowerToVo(content);
        Pagination<FollowerVo> pagination = new Pagination<>();
        BeanUtils.copyProperties(new Pagination<Follower>(page), pagination, "result");
        pagination.setResult(vos);
        return pagination;
    }

    @Override
    public FollowerVo getFollowerByAppIdAndId(WechatPublicNo pbNo, Long id) {
        Follower follower = Optional.ofNullable(followerDaoImpl.findByIdAndPubNoAppId(id, pbNo.getAuthorizerAppid()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        return convertFollowerToVo(follower);
    }

    @Override
    public FollowerVo getFollowerInfo(WechatPublicNo pbNo, String id) {
        Follower follower = Optional
                .ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(id, pbNo.getAuthorizerAppid()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        return convertFollowerToVo(follower);
    }

    private List<FollowerVo> convertFollowerToVo(List<Follower> content) {
        List<FollowerVo> vos = new ArrayList<>();
        if (content == null || content.size() == 0) {
            return vos;
        }
        content.forEach((follower) -> {
            vos.add(convertFollowerToVo(follower));
        });
        return vos;
    }

    private FollowerVo convertFollowerToVo(Follower follower) {
        FollowerVo vo = new FollowerVo();
        BeanUtils.copyProperties(follower, vo, FollowerVo.class);
        return vo;
    }

    @Override
    public Pagination<FollowerVo> searchByMultiCondition(FollowerSearchVo condition, WechatPublicNo pbNo,
            Pageable pageable) {
        Page<Follower> page = followerDaoImpl.findAll(generateSpecifications(condition, pbNo), pageable);
        List<Follower> content = page.getContent();
        List<FollowerVo> vos = convertFollowerToVo(content);
        Pagination<FollowerVo> pagination = new Pagination<>();
        BeanUtils.copyProperties(new Pagination<Follower>(page), pagination, "result");
        pagination.setResult(vos);
        return pagination;
    }

    @Override
    public Long countByMultiCondition(FollowerSearchVo condition, WechatPublicNo pbNo) {
        return followerDaoImpl.count(generateSpecifications(condition, pbNo));
    }

    private Specifications<Follower> generateSpecifications(FollowerSearchVo condition, WechatPublicNo pbNo) {
        Specifications<Follower> specifications = Specifications
                .where(FollowerCustomerSpecs.pbNoIdIs(pbNo.getAuthorizerAppid()));
        if (condition.getSex() != -1) {
            specifications = specifications.and(FollowerCustomerSpecs.sexIs(condition.getSex()));
        }

        if (condition.getSubscribe() != -1) {
            specifications = specifications.and(FollowerCustomerSpecs.isSubscribe(condition.getSubscribe()));
        }

        if (condition.getTagGroupConditions() != null && condition.getTagGroupConditions().size() > 0) {
            specifications = specifications
                    .and(FollowerCustomerSpecs.tagGroupCondition(condition.getTagGroupConditions()));
        }

        if (!StringUtils.isEmpty(condition.getStartDate()) && !StringUtils.isEmpty(condition.getEndDate())) {
            LocalDateTime startDate = LocalDateTime.parse(condition.getStartDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime endDate = LocalDateTime.parse(condition.getEndDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (startDate.isAfter(endDate)) {
                throw new ServiceWarn("StartDate must greate than endDate");
            }
            specifications = specifications
                    .and(FollowerCustomerSpecs.subscribeTimeBetween(getTime(startDate), getTime(endDate)));
        }
        return specifications;
    }

    private Long getTime(LocalDateTime date) {
        Calendar c = Calendar.getInstance();
        c.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), date.getHour(), date.getMinute(),
                date.getSecond());
        return Long.valueOf(c.getTimeInMillis() / 1000);
    }

    private final String ALREADY_REACH_WEIXIN_20_LIMIT = "达到微信20标签上限";
    private final String NOT_SUBSCRIBE = "未关注";

    @Override
    public void tagFollower(String openid, Set<Tag> tags, String pubNoAppId) {
        Follower follower = followerDaoImpl.findByOpenidAndPubNoAppId(openid, pubNoAppId);
        if (follower == null) {
            return;
        }
        WechatPublicNo pbNo = pbNoServiceImpl.findOneByAuthorizerAppid(pubNoAppId);
        if (pbNo == null) {
            return;
        }
        List<Long> ids = new ArrayList<>();
        ids.add(follower.getId());
        tagFollowers(ids, tags, pbNo);
    }

    // TODO： 这段代码太乱后期有时间需要重构
    @Override
    public List<FollowerTagResult> tagFollowers(List<Long> followerIds, Set<Tag> tags, WechatPublicNo pbNo) {
        List<Follower> followers = followerDaoImpl.findByIdInAndPubNoAppId(followerIds, pbNo.getAuthorizerAppid());
        long[] tagIds = tags.parallelStream().mapToLong((tag) -> {
            return tag.getId();
        }).toArray();
        List<Tag> dbTags = tagServiceImpl.getTagByIds(tagIds, pbNo);
        List<FollowerTagResult> results = new ArrayList<>();
        if (dbTags == null || dbTags.size() == 0 || followers == null || followers.size() == 0) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }

        for (Tag tag : dbTags) {
            FollowerTagResult result = new FollowerTagResult();
            result.setTag(tag);
            result.setTotal(followers.size());
            if (tag.isFromWechat()) {
                List<Follower> filtered = new ArrayList<>();
                followers.forEach((follower) -> {
                    Set<Tag> ft = follower.getTags();
                    // Not tag to unsubscribeFollower and Already has MoreThan20
                    if (ft != null && ft.stream().filter((dbTag) -> {
                        return dbTag.isFromWechat();
                    }).count() >= 20) {
                        if (result.getFailed().containsKey(ALREADY_REACH_WEIXIN_20_LIMIT)) {
                            result.getFailed().get(ALREADY_REACH_WEIXIN_20_LIMIT).add(follower);
                        } else {
                            List<Follower> failedFollowers = new ArrayList<>();
                            failedFollowers.add(follower);
                            result.getFailed().put(ALREADY_REACH_WEIXIN_20_LIMIT, failedFollowers);
                        }
                    } else if (follower.getSubscribe() == 0) {
                        if (result.getFailed().containsKey(NOT_SUBSCRIBE)) {
                            result.getFailed().get(NOT_SUBSCRIBE).add(follower);
                        } else {
                            List<Follower> failedFollowers = new ArrayList<>();
                            failedFollowers.add(follower);
                            result.getFailed().put(NOT_SUBSCRIBE, failedFollowers);
                        }
                    } else {
                        filtered.add(follower);
                    }
                });

                tagFilteredFollowerToWeixin(tag, filtered, result, pbNo);
                tagSuccessToDb(result.getSuccess(), tag);
            } else {
                // List<Follower> filtered = new ArrayList<>();
                // followers.forEach((follower) -> {
                // // Not tag to unsubscribeFollower
                // if (follower.getSubscribe() == 0) {
                // result.getFailed().add(follower);
                // } else {
                // filtered.add(follower);
                // }
                // });
                tagToDb(followers, tag, result);
            }

            results.add(result);
        }

        return results;
    }

    private void tagToDb(List<Follower> filtered, Tag tag, FollowerTagResult result) {
        filtered.forEach((follower) -> {
            if (!follower.getTags().contains(tag)) {
                follower.getTags().add(tag);
                tag.setCount(tag.getCount() + 1);
            }
        });
        followerDaoImpl.save(filtered);
        tagServiceImpl.save(tag);
        result.setSuccess(filtered);
    }

    private void tagSuccessToDb(List<Follower> success, Tag tag) {
        success.forEach((follower) -> {
            if (!follower.getTags().contains(tag)) {
                follower.getTags().add(tag);
                tag.setCount(tag.getCount() + 1);
            }
        });
        followerDaoImpl.save(success);
        tagServiceImpl.save(tag);
    }

    private void tagFilteredFollowerToWeixin(Tag tag, List<Follower> filtered, FollowerTagResult result,
            WechatPublicNo pbNo) {
        int start = 0;
        int end = filtered.size() - 1;
        int max = filtered.size() - 1;
        if (max >= 49) {
            end = 49;
        }
        while (start <= max) {
            List<Follower> followerNeedToTag = filtered.subList(start, end + 1);
            String[] openids = followerNeedToTag.parallelStream().map((follower) -> {
                return follower.getOpenid();
            }).collect(Collectors.toList()).toArray(new String[followerNeedToTag.size()]);
            BaseResult weixinResult = UserAPI.tagsMembersBatchtagging(
                    tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()),
                    tag.getTagId().intValue(), openids);
            if (weixinResult.isSuccess()) {
                result.getSuccess().addAll(followerNeedToTag);
            } else {
                String key = "Tag:" + tag.getName() + " ErrorCode:" + weixinResult.getErrcode() + " ErrorMessage:"
                        + weixinResult.getErrmsg();
                if (result.getFailed().containsKey(key)) {
                    result.getFailed().get(key).addAll(followerNeedToTag);
                } else {
                    List<Follower> failedFollowers = new ArrayList<>();
                    failedFollowers.addAll(followerNeedToTag);
                    result.getFailed().put(key, failedFollowers);
                }
            }
            start = end + 1;
            end += 49;
            if (end > max) {
                end = max;
            }
        }
    }

    @Override
    public void removeTagFromFollower(Set<Tag> tags, Long id, WechatPublicNo pbNo) {
        long[] tagIds = tags.parallelStream().mapToLong((tag) -> {
            return tag.getId();
        }).toArray();
        List<Tag> dbTags = tagServiceImpl.getTagByIds(tagIds, pbNo);
        if (dbTags == null || dbTags.size() == 0) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        Follower dbFollower = followerDaoImpl.findByIdAndPubNoAppId(id, pbNo.getAuthorizerAppid());
        if (dbFollower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }
        dbTags.forEach((tag) -> {
            if (dbFollower.getTags().contains(tag)) {
                if (tag.isFromWechat()) {
                    String[] openids = new String[] { dbFollower.getOpenid() };
                    BaseResult result = UserAPI.tagsMembersBatchuntagging(
                            tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()),
                            tag.getTagId().intValue(), openids);
                    if (result.isSuccess()) {
                        dbFollower.getTags().remove(tag);
                        tag.setCount(tag.getCount() - 1);
                    } else {
                        throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
                    }
                } else {
                    dbFollower.getTags().remove(tag);
                    tag.setCount(tag.getCount() - 1);
                }
                tagServiceImpl.save(tag);
            }
        });
        followerDaoImpl.save(dbFollower);
    }

    @Override
    public String exportByMultiCondition(FollowerSearchVo condition, WechatPublicNo pbNo) throws Exception {
        ExportLog log = new ExportLog();
        log.setStatus(ExportLog.PROGRESSING);
        log.setWechatPublicNoId(pbNo.getId());
        log = exportLogDaoImpl.save(log);
        Long logid = log.getId();
        AsyncUtil.asyncRun(() -> {
            runExportJob(condition, pbNo, logid);
        });
        return log.getId() + "";
    }

    private void runExportJob(FollowerSearchVo condition, WechatPublicNo pbNo, Long logid) {
        ExportLog log = exportLogDaoImpl.findOne(logid);
        String path = "";
        try {
            logger.info("======= start export follower ====" + logid);
            List<Follower> followers = followerDaoImpl.findAll(generateSpecifications(condition, pbNo));
            logger.info("=======  export follower get end ====" + followers.size() + " logid " + logid);
            List<FollowerExportVo> vos = convertFollowersToExportVos(followers, pbNo);
            logger.info("=======  export end convert follower to ExportVos ====" + vos.size() + " logid " + logid);

            logger.info("======= export follower to file ====" + logid);

            InputStream ins = resourceLoader.getResource("classpath:template/follower_template.xlsx").getInputStream();
            path = exportPath + "follower_export_" + System.currentTimeMillis() + ".xlsx";
            FileOutputStream os = new FileOutputStream(new File(path));
            Workbook workbook = WorkbookFactory.create(ins);
            PoiTransformer transformer = PoiTransformer.createSxssfTransformer(workbook, 5, true);
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            Context context = new Context();
            context.putVar("followers", vos);
            logger.info("======= before export follower to file ====" + logid);
            xlsArea.applyAt(new CellRef("result!A1"), context);
            transformer.getWorkbook().removeSheetAt(0);
            transformer.getWorkbook().write(os);
            ins.close();
            os.close();
            logger.info("======= end export follower to file ====" + logid);
        } catch (FileNotFoundException e) {
            log.setStatus(ExportLog.ERROR);
            e.printStackTrace();
            logger.info("======= export follower error ====" + logid);
            logger.info(e.getStackTrace().toString());
            exportLogDaoImpl.save(log);
            return;
        } catch (IOException e) {
            log.setStatus(ExportLog.ERROR);
            e.printStackTrace();
            logger.info("======= export follower error ====" + logid);
            logger.info(e.getStackTrace().toString());
            exportLogDaoImpl.save(log);
            return;
        } catch (Exception e) {
            log.setStatus(ExportLog.ERROR);
            logger.info("======= export follower error ====" + logid);
            logger.info(e.getStackTrace().toString());
            exportLogDaoImpl.save(log);
            return;
        }

        log.setPath(path);
        log.setStatus(ExportLog.FINISHED);
        exportLogDaoImpl.save(log);
        logger.info("======= end export follower ====" + logid);
    }

    private List<FollowerExportVo> convertFollowersToExportVos(List<Follower> followers, WechatPublicNo pbNo)
            throws Exception {
        List<FollowerExportVo> vos = new ArrayList<>();
        Map<String, AIAUserInfo> infoMap = new HashMap<>();
        Iterable<AIAUserInfo> allUserInfo = aiaUserInfoDaoImpl.findAll();
        logger.info("=======  export start convert aiaUserinfo to map  ====");
        allUserInfo.forEach((info) -> {
            infoMap.put(info.getOpenid(), info);
        });
        logger.info("=======  export end convert aiaUserinfo to map  ====" + infoMap.size());
        if (followers == null || followers.size() == 0) {
            return vos;
        }
        logger.info("=======  export start convert follower toExport  ====" + followers.size());
        for (Follower f : followers) {
            vos.add(convertFollowerToExportVo(f, pbNo, infoMap.get(f.getOpenid())));
        }
        logger.info("=======  export end convert follower toExport  ====" + vos.size());
        return vos;
    }

    private FollowerExportVo convertFollowerToExportVo(Follower f, WechatPublicNo pbNo, AIAUserInfo info)
            throws Exception {
        FollowerExportVo vo = new FollowerExportVo();
        BeanUtils.copyProperties(f, vo, "nickname", "sex", "subscribe", "subscribeTime", "groupid");
        vo.setWechatPublicNoName(pbNo.getNickName());
        vo.setNickname(f.getNickname());
        vo.setGroupid(f.getGroupid() + "");
        String sex = "未知";
        if (f.getSex() != null && f.getSex() == 1) {
            sex = "男";
        } else if (f.getSex() != null && f.getSex() == 2) {
            sex = "女";
        }
        vo.setSex(sex);
        String subscribe = (f.getSubscribe() != null && f.getSubscribe() == 1) ? "关注" : "未关注";
        vo.setSubscribe(subscribe);

        if (f.getSubscribeTime() != null) {
            vo.setSubscribeTime(TimeUtil.formatYYYYMMDDHHMMSS(f.getSubscribeTime().longValue() * 1000));
        }

        if (info == null) {
            return vo;
        }
        vo.setProfile_city(info.getCity());
        vo.setProfile_district(info.getDistrict());
        vo.setProfile_phone(info.getPhone());
        vo.setProfile_province(info.getProvince());
        return vo;
    }

    @Override
    public ExportLog exportPollingCheckResult(Long id, WechatPublicNo pbNo) {
        return exportLogDaoImpl.findByIdAndWechatPublicNoId(id, pbNo.getId());
    }

    @Override
    public FollowerRelatedVo checkFollowerRelated(FollowerCheckVo filters, String openid) throws Exception {
        FollowerRelatedVo vo = new FollowerRelatedVo();
        vo.setFollowed(false);
        vo.setEnrolled(false);
        if (filters.isCheckFollower()) {
            Optional<Follower> follower = Optional.ofNullable(this.findOneByOpenid(openid));

            if (follower.isPresent() && follower.get().getSubscribe().intValue() != 0) {
                vo.setFollowed(true);
                vo.setFollower(follower.get());
            }
        }

        if (filters.isCheckMember()) {
            Optional<FollowerBindInfo> member = Optional
                    .ofNullable(followerBindInfoServiceImpl.findOneByOpenid(openid));
            if (member.isPresent()) {
                vo.setEnrolled(true);
                vo.setMember(member.get());
            }
        }

        return vo;
    }

}