package com.merkle.wechat.modules.digikey.service;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.WechatPublicNoDao;
import com.merkle.wechat.common.dao.digikey.DigikeyCalenderCampaignDao;
import com.merkle.wechat.common.dao.follower.FollowerBindInfoDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.digikey.DigikeyCalenderCampaign;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.follower.FollowerShipAddress;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.follower.FollowerBindInfoService;
import com.merkle.wechat.service.follower.FollowerShipAddressService;

@Component
public class DigikeyCalenderCampaignServiceImpl {
    private @Autowired DigikeyCalenderCampaignDao digikeyCalenderCampaignDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired WechatPublicNoDao pbNoDaoImpl;
    private @Autowired FollowerBindInfoDao followerBindInfoDaoImpl;
    private @Autowired FollowerBindInfoService bindInfoServiceImpl;
    private @Autowired FollowerShipAddressService followerShipAddressServiceImpl;

    public void save(DigikeyCalenderCampaign campaign) throws Exception {
        boolean exists = false;
        Follower follower = Optional.ofNullable(followerDaoImpl.findOneByOpenid(campaign.getOpenid()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        if (StringUtils.isNotEmpty(follower.getUnionid())) {
            exists = digikeyCalenderCampaignDaoImpl.existsByUnionid(follower.getUnionid());
        } else {
            exists = digikeyCalenderCampaignDaoImpl.existsByOpenid(campaign.getOpenid());
        }
        if (exists) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }
        WechatPublicNo pbNo = pbNoDaoImpl.findOneByAuthorizerAppid(follower.getPubNoAppId());
        campaign.setUnionid(follower.getUnionid());
        digikeyCalenderCampaignDaoImpl.save(campaign);

        FollowerShipAddress address = new FollowerShipAddress();
        address.setAddress(campaign.getAddress());
        address.setName(campaign.getName());
        address.setOpenid(campaign.getOpenid());
        address.setPhone(campaign.getPhone());
        followerShipAddressServiceImpl.create(address);

        FollowerBindInfo bindInfo = followerBindInfoDaoImpl.findOneByOpenid(campaign.getOpenid());
        if (bindInfo == null) {
            FollowerBindInfo info = new FollowerBindInfo();
            info.setAddress(campaign.getAddress());
            info.setAppId(pbNo.getAuthorizerAppid());
            info.setEmail(campaign.getEmail());
            info.setName(campaign.getName());
            info.setOpenid(campaign.getOpenid());
            info.setPhone(campaign.getPhone());
            info.setQq(campaign.getQq());
            info.setTitle(campaign.getCareer());
            info.setWechatPublicNoId(pbNo.getId());
            bindInfoServiceImpl.create(info);
        } else {
            if (StringUtils.isEmpty(bindInfo.getAddress())) {
                bindInfo.setAddress(campaign.getAddress());
            }

            if (StringUtils.isEmpty(bindInfo.getEmail())) {
                bindInfo.setEmail(campaign.getEmail());
            }

            if (StringUtils.isEmpty(bindInfo.getPhone())) {
                bindInfo.setPhone(campaign.getPhone());
            }

            if (StringUtils.isEmpty(bindInfo.getName())) {
                bindInfo.setName(campaign.getName());
            }

            if (StringUtils.isEmpty(bindInfo.getQq())) {
                bindInfo.setQq(campaign.getQq());
            }

            if (StringUtils.isEmpty(bindInfo.getTitle())) {
                bindInfo.setTitle(campaign.getCareer());
            }
            bindInfoServiceImpl.update(bindInfo.getId(), bindInfo);
        }
    }

    public boolean check(String openid) {
        return digikeyCalenderCampaignDaoImpl.existsByOpenid(openid);
    }

    public boolean checkByUnionid(String unionid) {
        return digikeyCalenderCampaignDaoImpl.existsByUnionid(unionid);
    }

    public DigikeyCalenderCampaign getByOpenid(String openid) throws Exception {
        boolean exists = digikeyCalenderCampaignDaoImpl.existsByOpenid(openid);
        if (!exists) {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }
        return digikeyCalenderCampaignDaoImpl.findByOpenid(openid);
    }

}
