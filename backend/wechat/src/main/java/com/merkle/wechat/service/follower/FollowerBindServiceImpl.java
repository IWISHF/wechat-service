package com.merkle.wechat.service.follower;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.common.dao.follower.FollowerBindInfoDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.dao.follower.MemberAttributeDao;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.digikey.service.DigikeyService;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.service.TagService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.follower.FollowerApiInfoVo;

@Component
public class FollowerBindServiceImpl implements FollowerBindInfoService {
    protected Logger logger = LoggerFactory.getLogger("FollowerBindServiceImpl");
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired FollowerBindInfoDao followerBindInfoDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired DigikeyService digikeyServiceImpl;
    private @Autowired TagService tagServiceImpl;
    private @Autowired MemberAttributeDao memberAttributeDao;
    @Value("${wechat.official.account.appid}")
    private String appid;

    public final static String INCLUDE_DELIMITER = ",";
    public final static String INCLUDE_ATTRIBUTES = "member_attributes";

    @Override
    public FollowerBindInfo findOneByOpenid(String openid) {
        Follower f = followerDaoImpl.findOneByOpenid(openid);
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            if (f.getPubNoAppId().equals(this.appid)) {
                WechatPublicNo pbNo = pbNoServiceImpl.findOneByAuthorizerAppid(this.appid);
                List<Follower> fs = followerDaoImpl.findAllByUnionid(f.getUnionid());
                for (Follower follower : fs) {
                    Tag tag = tagServiceImpl.getTagByName("digikeyVipFollower", pbNo);
                    if (!follower.getTags().contains(tag) && !follower.getSubscribeScene().equals("MINI_PROGRAM")) {
                        digikeyServiceImpl.tagVipForDigikey(follower.getOpenid(), pbNo);
                    }
                }
            }
            return followerBindInfoDaoImpl.findOneByUnionid(f.getUnionid());
        }
        return followerBindInfoDaoImpl.findOneByOpenid(openid);
    }

    @Override
    public void create(FollowerBindInfo bindInfo) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(bindInfo.getWechatPublicNoId());
        bindInfo.setAppId(pbNo.getAuthorizerAppid());
        Follower f = Optional
                .ofNullable(followerDaoImpl.findOneByOpenidAndPubNoAppId(bindInfo.getOpenid(), bindInfo.getAppId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        boolean exist = false;
        if (!StringUtils.isEmpty(f.getUnionid())) {
            exist = followerBindInfoDaoImpl.existsByUnionid(f.getUnionid());
        } else {
            exist = followerBindInfoDaoImpl.existsByOpenid(bindInfo.getOpenid());
        }
        if (exist) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }
        bindInfo.setUnionid(f.getUnionid());
        bindInfo = followerBindInfoDaoImpl.save(bindInfo);
        ResponseData response = loyaltyServiceImpl.syncBindInfoToLoyalty(bindInfo);
        if (null != response && response.isSuccess()) {
            bindInfo.setSyncToLoyalty(true);
            followerBindInfoDaoImpl.save(bindInfo);
            loyaltyServiceImpl.recordBindInfoEvent(bindInfo.getOpenid(), bindInfo.getAppId());
            if (completeProfile(bindInfo)) {
                loyaltyServiceImpl.recordCompleteProfileEvent(bindInfo.getOpenid(), bindInfo.getAppId());
            }
            if (bindInfo.getAppId().equals(this.appid)) {
                digikeyServiceImpl.tagVipForDigikey(bindInfo.getOpenid(), pbNo);
            }
        }
    }

    @Override
    public void fixSyncFollowerInfoFailed() throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findOneByAuthorizerAppid(this.appid);
        List<FollowerBindInfo> infos = followerBindInfoDaoImpl.findBySyncToLoyalty(false);
        for (FollowerBindInfo bindInfo : infos) {
            ResponseData response = loyaltyServiceImpl.syncBindInfoToLoyalty(bindInfo);
            if (null != response && response.isSuccess()) {
                bindInfo.setSyncToLoyalty(true);
                followerBindInfoDaoImpl.save(bindInfo);
                loyaltyServiceImpl.recordBindInfoEvent(bindInfo.getOpenid(), bindInfo.getAppId());
                if (completeProfile(bindInfo)) {
                    loyaltyServiceImpl.recordCompleteProfileEvent(bindInfo.getOpenid(), bindInfo.getAppId());
                }
                if (bindInfo.getAppId().equals(this.appid)) {
                    digikeyServiceImpl.tagVipForDigikey(bindInfo.getOpenid(), pbNo);
                }
            }
        }
    }

    private boolean completeProfile(FollowerBindInfo bindInfo) {
        return !(StringUtils.isEmpty(bindInfo.getAddress()) || StringUtils.isEmpty(bindInfo.getCurrency())
                || StringUtils.isEmpty(bindInfo.getEmail()) || StringUtils.isEmpty(bindInfo.getName())
                || StringUtils.isEmpty(bindInfo.getPhone()) || StringUtils.isEmpty(bindInfo.getQq())
                || StringUtils.isEmpty(bindInfo.getTitle()));
    }

    @Override
    public void update(Long id, FollowerBindInfo bindInfo) throws Exception {
        FollowerBindInfo dbBindInfo = Optional.ofNullable(followerBindInfoDaoImpl.findOne(id))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        bindInfo.setWechatPublicNoId(dbBindInfo.getWechatPublicNoId());
        bindInfo.setAppId(dbBindInfo.getAppId());
        bindInfo.setId(dbBindInfo.getId());
        bindInfo.setUpdatedDate(new Date());
        bindInfo.setUnionid(dbBindInfo.getUnionid());
        BeanUtils.copyProperties(bindInfo, dbBindInfo);
        dbBindInfo.setSyncToLoyalty(false);
        dbBindInfo = followerBindInfoDaoImpl.save(dbBindInfo);
        ResponseData response = loyaltyServiceImpl.syncBindInfoToLoyalty(dbBindInfo);
        if (null != response && response.isSuccess()) {
            bindInfo.setSyncToLoyalty(true);
            followerBindInfoDaoImpl.save(bindInfo);
            if (completeProfile(bindInfo)) {
                loyaltyServiceImpl.recordCompleteProfileEvent(bindInfo.getOpenid(), bindInfo.getAppId());
            }
            if (bindInfo.getAppId().equals(this.appid)) {
                digikeyServiceImpl.tagVipForDigikey(bindInfo.getOpenid(),
                        pbNoServiceImpl.findByIdOrThrowNotExistException(bindInfo.getWechatPublicNoId()));
            }
        }
    }

    @Override
    public FollowerApiInfoVo getFollowerInfo(String id) throws Exception {
        FollowerApiInfoVo vo = new FollowerApiInfoVo();
        Follower dbFollower = Optional.ofNullable(followerDaoImpl.findOneByOpenid(id))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        BeanUtils.copyProperties(dbFollower, vo);
        FollowerBindInfo bindInfo = null;
        if (!StringUtils.isEmpty(dbFollower.getUnionid())) {
            bindInfo = followerBindInfoDaoImpl.findOneByUnionid(dbFollower.getUnionid());
        } else {
            bindInfo = followerBindInfoDaoImpl.findOneByOpenid(id);
        }
        if (null != bindInfo) {
            vo.setBindInfo(bindInfo);
        }

        return vo;
    }

    @Override
    public FollowerApiInfoVo findFollowerWithIncludes(String openid, String includes) throws Exception {
        FollowerApiInfoVo vo = new FollowerApiInfoVo();
        Follower dbFollower = Optional.ofNullable(followerDaoImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        BeanUtils.copyProperties(dbFollower, vo);
        FollowerBindInfo bindInfo = null;
        if (!StringUtils.isEmpty(dbFollower.getUnionid())) {
            bindInfo = followerBindInfoDaoImpl.findOneByUnionid(dbFollower.getUnionid());
        } else {
            bindInfo = followerBindInfoDaoImpl.findOneByOpenid(openid);
        }
        if (null != bindInfo) {
            vo.setBindInfo(bindInfo);
        }

        if (null != includes) {
            String[] includesArr = includes.split(INCLUDE_DELIMITER);
            for (String include : includesArr) {
                if (INCLUDE_ATTRIBUTES.equals(include)) {
                    vo.setAttributes(memberAttributeDao.findByOpenid(openid));
                }
            }
        }
        return vo;
    }

}
