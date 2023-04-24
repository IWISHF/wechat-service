package com.merkle.wechat.modules.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.loyalty.apis.CustomerAPI;
import com.merkle.loyalty.apis.EventsAPI;
import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.constant.LoyaltyExceptionConstants;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyRewardsRedeemVo;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.loyalty.LoyaltyConfigServiceImpl;

@Component
public class DemoLoyaltyServiceImpl implements DemoLoyaltyService {

    protected Logger logger = LoggerFactory.getLogger("DemoLoyaltyServiceImpl");

    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;

    private @Autowired FollowerService followerServiceImpl;

    private @Autowired DemoTagService tagServiceImpl;

    private @Autowired LoyaltyConfigServiceImpl loyaltyConfigServiceImpl;

    private @Autowired LoyaltyService loyaltyServiceImpl;

    @Override
    public void syncFollowerToLoyalty(String openid, String appId) throws Exception {
        Follower follower = followerServiceImpl.findOrCreateFollower(openid, appId);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }
        boolean needSync = follower != null && !follower.isRecordToLoyaltySuccess();
        if (needSync) {
            syncFollowerToLoyalty(follower);
        }
    }
    
    @Override
    public void syncFollowerToLoyalty(Follower follower) throws Exception {
        boolean success = registerToLoyalty(follower);
        if (success) {
            follower.setRecordToLoyaltySuccess(success);
            followerServiceImpl.updateFollower(follower);
        } else {
            throw new ServiceWarn(LoyaltyExceptionConstants.SYNC_FOLLOWER_TO_LOYALTY_FAILED);
        }
    }

    @Override
    public void subscribePublicNoUpate(String openid, String appId) throws Exception {
        Follower follower = followerServiceImpl.findOrCreateFollower(openid, appId);
        logger.info("++++++++++++++++++ subscribe update ========+++++++++");
        Map<String, String> params = new HashMap<String, String>();
        String unionId = getUnionId(follower);
        params.put("external_customer_id", unionId);
        params.put("name", follower.getNickname());
        params.put("channel", "WeChat");
        params.put("status", "active");

        PrismResponse response = CustomerAPI.enrollOrUpdate(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        logger.info("+++++++==  ======= subscribe update response" + response);

        if (response.toPrismData().isSuccess()) {
            follower.setRecordToLoyaltySuccess(true);
            followerServiceImpl.updateFollower(follower);
        }
    }

    @Override
    public void unSubscribePublicNoUpate(String openid, String appId) throws Exception {
        Follower follower = followerServiceImpl.findOrCreateFollower(openid, appId);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        logger.info("++++++++++++++++++ unsubscribe start ========+++++++++");
        Map<String, String> params = new HashMap<String, String>();
        String unionId = getUnionId(follower);
        WechatPublicNo wechatPublicNo = wechatPbNoServiceImpl.findOneByAuthorizerAppid(follower.getPubNoAppId());
        params.put("external_customer_id", unionId);
        params.put("new_vendor", "wechat_" + wechatPublicNo.getNickName());
        params.put("new_vendor_id", "false_" + follower.getOpenid());
        params.put("channel", "WeChat");
        params.put("status", "pending");

        PrismResponse response = CustomerAPI.enrollOrUpdate(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        logger.info("+++++++==  ======= unsubscribe response" + response);

        if (response.toPrismData().isSuccess()) {
            follower.setRecordToLoyaltySuccess(true);
            followerServiceImpl.updateFollower(follower);
        }
    }

    /**
     * 
     * @param follower
     *            - follower must not be null
     * @return
     * @throws Exception
     */
    private void makeSureFollowerAlreadySyncToLoyalty(String openid, String appId) throws Exception {
        syncFollowerToLoyalty(openid, appId);
    }

    @Override
    public PrismResponse getCustomerInfoFromLoyalty(String openid) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("include", "badges,coupons,offers,rewards,member_attributes,identities,reward_stats");
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        PrismResponse response = CustomerAPI
                .customerShow2016(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        ResponseData prismData = response.toPrismData();
        Object att = prismData.getData().getDynamicProperties().get("member_attributes");

        if (att != null) {
            @SuppressWarnings("unchecked")
            Map<String, Integer> memberAttributes = (Map<String, Integer>) att;

            Integer p1 = memberAttributes.get("p1_interest_score");
            Integer p5 = memberAttributes.get("p5_interest_score");

            if (p1 != null && p1 >= 80) {
                Set<Tag> tags = follower.getTags();
                boolean match = tags.stream().anyMatch((t) -> {
                    return t.getName().equals("p1_interest_score_80");
                });
                if (!match) {
                    tagServiceImpl.tagFollower(openid, "p1_interest_score_80");
                    LoyaltyRewardsRedeemVo vo = new LoyaltyRewardsRedeemVo();
                    vo.setId(openid);
                    vo.setRewardGroup("ecoupon");
                    vo.setRewardId("151");
                    vo.setRewardName("PlayOne 100元优惠券");
                    vo.setRewardType("1");
                    loyaltyServiceImpl.redeemReward(vo);
                }
            } else {
                tagServiceImpl.removeTagFromFollower(openid, "p1_interest_score_80");
            }

            if (p5 != null && p5 >= 80) {
                tagServiceImpl.tagFollower(openid, "p5_interest_score_80");
            } else {
                tagServiceImpl.removeTagFromFollower(openid, "p5_interest_score_80");
            }
        }

        return response;
    }

    @Override
    public PrismResponse recordMenuClick(String openid, String value, String publicNoAppId) throws Exception {
        makeSureFollowerAlreadySyncToLoyalty(openid, publicNoAppId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "wechat-menu");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("value", value);
        params.put("channel", "WeChat");

        PrismResponse response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(publicNoAppId), params);
        return response;
    }

    @Override
    public PrismResponse recordSubscribe(String openid, String publicNoAppId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "wechat-subscribe");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("detail", publicNoAppId);
        params.put("channel", "WeChat");

        PrismResponse response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(publicNoAppId), params);
        return response;
    }

    @Override
    public PrismResponse recordPurchase(String openid, String value, String refId, String eventId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "purchase");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("value", value);
        params.put("event_id", eventId);
        params.put("primary_reference_id", refId);
        params.put("channel", "WeChat");

        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        PrismResponse response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()),
                params);
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse redeem(String openid, String value) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "redeem");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("value", value);
        params.put("channel", "WeChat");

        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        PrismResponse response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()),
                params);
        System.out.println(response);
        return response;
    }

    /**
     * wechat-scan, wechat-link, wechat-unsubscribe, wechat-text
     * 
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public PrismResponse recordWXEvent(String type, String openid, String publicNoAppId) throws Exception {
        // makeSureFollowerAlreadySyncToLoyalty(openid, publicNoAppId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("detail", publicNoAppId);
        params.put("channel", "WeChat");

        PrismResponse response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(publicNoAppId), params);
        System.out.println(response);
        return response;
    }

    /**
     * Like, view-detail, compare, wechat-pv
     * 
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public PrismResponse recordCustomerEvent(String type, String openid) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "WeChat");

        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        PrismResponse response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()),
                params);
        System.out.println(response);
        return response;
    }

    private boolean registerToLoyalty(Follower follower) throws Exception {
        logger.info("++++++++++++++++++ register start ========+++++++++");
        Map<String, String> params = new HashMap<String, String>();
        String unionId = getUnionId(follower);
        WechatPublicNo wechatPublicNo = wechatPbNoServiceImpl.findOneByAuthorizerAppid(follower.getPubNoAppId());
        params.put("external_customer_id", unionId);
        params.put("new_vendor", "wechat_" + wechatPublicNo.getNickName());
        params.put("new_vendor_id", follower.getOpenid());
        params.put("channel", "WeChat");
        // params.put("image_url", follower.getHeadimgurl());
        params.put("name", follower.getNickname());
        params.put("status", "active");

        PrismResponse response = CustomerAPI
                .enrollOrUpdate(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        logger.info("+++++++==  ======= register response" + response);
        return response.toPrismData().isSuccess();
    }

    @Override
    public PrismResponse getCustomerEvents(String openid, String pageNumber, String pageSize) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("page_number", pageNumber);
        params.put("entries_per_page", pageSize);
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        PrismResponse response = CustomerAPI
                .customerEvents(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response;
    }

    @Override
    public PrismResponse redeemReward(String openid, String rewardId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("reward_id", rewardId);

        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        PrismResponse response = EventsAPI
                .redeemReward(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response;
    }

    private String getUnionId(Follower follower) {
        return follower.getOpenid();
    }
}
