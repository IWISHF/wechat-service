package com.merkle.wechat.service.follower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.service.TagService;
import com.merkle.wechat.service.TokenServiceImpl;
import com.merkle.wechat.service.WechatPublicNoService;

import weixin.popular.api.UserAPI;
import weixin.popular.bean.user.FollowResult;
import weixin.popular.bean.user.FollowResult.Data;
import weixin.popular.bean.user.User;
import weixin.popular.bean.user.UserInfoList;

@Component
public class FollowerSyncServiceImpl implements FollowerSyncService {
    protected Logger logger = LoggerFactory.getLogger("FollowerSyncServiceImpl");

    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired TokenServiceImpl tokenServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired TagService tagServiceImpl;

    @Override
    public void syncLatestFollowerInfoFromWechat(String appId) throws Exception {
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

        Date before15Day = new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000L);
        Set<String> noNeedUpdateOpenids = followerDaoImpl.getDistinctOpenIdsByAppIdAndUpdatedDate(appId, before15Day);
        openids.removeAll(noNeedUpdateOpenids);
        logger.info("==== sync remove follower ======= openids.size:" + openids.size()
                + " ===== noNeedUpdateOpenids.size" + noNeedUpdateOpenids.size());
        noNeedUpdateOpenids.clear();

        int size = openids.size();
        totalNo = size;
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
        try {
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
                logger.info("===== sync follower total:" + totalNo + " from:" + (start + from) + " to:" + (start + to)
                        + " ====");
                List<Follower> followers = new ArrayList<>();
                UserInfoList userInfoList = UserAPI.userInfoBatchget(
                        tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), "zh_CN",
                        openids.subList(start + from, start + to + 1), 5);
                logger.info("===== sync follower total:" + totalNo + " from:" + from + " to:" + to
                        + " ==== userInfo success:" + userInfoList.isSuccess());
                if (userInfoList.isSuccess()) {
                    for (User user : userInfoList.getUser_info_list()) {
                        Follower follower = Optional.ofNullable(
                                followerDaoImpl.findByOpenidAndPubNoAppId(user.getOpenid(), pbNo.getAuthorizerAppid()))
                                .orElse(new Follower());
                        convertUserToFollower(user, follower, pbNo);
                        follower.setPubNoAppId(appId);
                        follower.setUpdatedDate(new Date());
                        followers.add(follower);
                    }
                    logger.info("===== sync follower total:" + totalNo + " from:" + from + " to:" + to
                            + " ==== start save");
                    followerDaoImpl.save(followers);
                    logger.info(
                            "===== sync follower total:" + totalNo + " from:" + from + " to:" + to + " ==== end save");
                    syncCount += to - from + 1;
                    from = to + 1;
                    to += 100;
                    if (to > size) {
                        to = size - 1;
                    }
                } else {
                    logger.info("===== sync follower Retry:" + totalNo + " from:" + (start + from) + " to:"
                            + (start + to) + " ====");
                    // throw new ServiceWarn(userInfoList.getErrmsg(),
                    // Integer.valueOf(userInfoList.getErrcode()));
                }
            }
        } catch (Exception e) {
            logger.error("====================== this is sync error =====");
            logger.info(e.getMessage());
            e.printStackTrace();
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
                    Follower follower = Optional.ofNullable(
                            followerDaoImpl.findByOpenidAndPubNoAppId(user.getOpenid(), pbNo.getAuthorizerAppid()))
                            .orElse(new Follower());

                    convertUserToFollower(user, follower, pbNo);
                    follower.setPubNoAppId(appId);
                    follower.setUpdatedDate(new Date());
                    followers.add(follower);
                }
                logger.info(
                        "===== sync follower total:" + totalNo + " from:" + from + " to:" + to + " ==== start save");
                followerDaoImpl.save(followers);
                logger.info("===== sync follower total:" + totalNo + " from:" + from + " to:" + to + " ==== end save");
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
                    if (!follower.getTags().contains(tag)) {
                        follower.getTags().add(tag);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        follower.setQrScene(userInfo.getQr_scene());
        follower.setQrSceneStr(userInfo.getQr_scene_str());
        follower.setPrivilegeStr(JSONUtil.objectJsonStr(userInfo.getPrivilege()));
    }

}
