package com.merkle.wechat.modules.loyalty.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.merkle.loyalty.apis.CustomerAPI;
import com.merkle.loyalty.apis.DataAPI;
import com.merkle.loyalty.apis.EventsAPI;
import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.response.ResponseData;
import com.merkle.loyalty.util.JsonUtil;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.digikey.DigikeyEventMultiChannel;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.tracking.TrackCampaignPageEvent;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.constant.LoyaltyExceptionConstants;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyEventsVo;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyRewardsRedeemVo;
import com.merkle.wechat.service.RewardsRedeemLogService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.loyalty.LoyaltyConfigServiceImpl;
import com.merkle.wechat.service.template.WeixinTemplateMessageService;

@Component
public class LoyaltyServiceImpl implements LoyaltyService {
    protected Logger logger = LoggerFactory.getLogger("LoyaltyServiceImpl");

    private @Autowired FollowerService followerServiceImpl;

    private @Autowired LoyaltyConfigServiceImpl loyaltyConfigServiceImpl;

    private @Autowired FollowerDao followerDaoImpl;

    private @Autowired RewardsRedeemLogService rewardsRedeemLogServciceImpl;

    private @Autowired WeixinTemplateMessageService weixinTemplateMessageServiceImpl;

    private String WX_TEXT_EVENT = "wechat-text";
    private String WX_LINK_EVENT = "wechat-link";
    private String WX_CLICK_EVENT = "wechat-click";

    /**
     *
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public PrismResponse recordWXEvent(String type, EventMessage event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getFromUserName()));
        params.put("detail", JSONUtil.objectJsonStr(event));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(event.getAppId()), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    /*
     * just use for digikey events now, event identity must be openid
     */
    @Override
    public PrismResponse recordDigikeyEvent(DigikeyEventMultiChannel event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", event.getEvent());
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getIdentityId()));
        params.put("value", event.getContent());
        params.put("detail", JSONUtil.objectJsonStr(event));
        params.put("event_id", event.getId() + "");
        params.put("channel", event.getChannelType() + "_track");
        params.put("sub_channel", "DIGIKEY");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByPbNoId(Long.valueOf(event.getChannelId())),
                    params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordCPE(TrackCampaignPageEvent event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", event.getEvent());
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getIdentityId()));
        params.put("value", event.getContent());
        params.put("detail", JSONUtil.objectJsonStr(event));
        params.put("event_id", event.getId() + "");
        params.put("channel", event.getChannelType() + "_track");
        params.put("sub_channel", "CPE");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByPbNoId(Long.valueOf(event.getChannelId())),
                    params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordTextEvent(EventMessage message) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", WX_TEXT_EVENT);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(message.getFromUserName()));
        params.put("value", message.getContent());
        params.put("detail", JSONUtil.objectJsonStr(message));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(message.getAppId()), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordLinkEvent(EventMessage message) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", WX_LINK_EVENT);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(message.getFromUserName()));
        params.put("value", message.getUrl());
        params.put("detail", JSONUtil.objectJsonStr(message));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(message.getAppId()), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordMenuClickEvent(EventMessage event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", WX_CLICK_EVENT);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getFromUserName()));
        params.put("value", event.getEventKey());
        params.put("detail", JSONUtil.objectJsonStr(event));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(event.getAppId()), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordScanEvent(EventMessage event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "wechat-scan");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getFromUserName()));
        params.put("value", event.getEventKey());
        params.put("detail", JSONUtil.objectJsonStr(event));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(event.getAppId()), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordMenuViewEvent(EventMessage event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "wechat-view");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getFromUserName()));
        params.put("value", event.getEventKey());
        params.put("detail", JSONUtil.objectJsonStr(event));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(event.getAppId()), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordUnsubscribeEvent(EventMessage event) {
        recordWXEvent("wechat-unsubscribe", event);

        Map<String, String> params = new HashMap<String, String>();
        params.put("op", "replace");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(event.getFromUserName()));
        params.put("path", "/subscribe_status");
        params.put("value", "false");

        PrismResponse response = null;
        try {
            response = CustomerAPI.customerUpdateAttributes(loyaltyConfigServiceImpl.getEnvByAppId(event.getAppId()),
                    params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    public PrismResponse recordSubscribeEvent(String openid, String appId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "wechat-subscribe");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "WeChat");
        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordEvent(String openid, String appId, String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "Wechat");
        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordEvent(String openid, String appId, String type, String value) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "Wechat");
        params.put("value", value);
        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordEvent(String openid, String appId, String type, String value, String detail) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "Wechat");
        params.put("value", value);
        params.put("detail", detail);
        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public void syncFollowerToLoyalty(String openid, String appId) throws Exception {
        Follower follower = followerServiceImpl.findOrCreateFollower(openid, appId);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }
        boolean needSync = follower != null && !follower.isRecordToLoyaltySuccess();
        if (needSync) {
            syncFollowerToLoyalty(follower);
        } else {

            Map<String, String> addAttributes = new HashMap<String, String>();
            addAttributes.put("op", "replace");
            addAttributes.put("external_customer_id", followerServiceImpl.getFollowerUnionId(follower.getOpenid()));
            addAttributes.put("path", "/subscribe_status");
            addAttributes.put("value", "true");
            CustomerAPI.customerUpdateAttributes(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()),
                    addAttributes);

            recordSubscribeEvent(openid, appId);

        }
    }

    private void syncFollowerToLoyalty(Follower follower) throws Exception {
        boolean success = registerToLoyalty(follower);
        if (success) {
            follower.setRecordToLoyaltySuccess(success);
            followerServiceImpl.updateFollower(follower);
            recordSubscribeEvent(follower.getOpenid(), follower.getPubNoAppId());
        } else {
            throw new ServiceWarn(LoyaltyExceptionConstants.SYNC_FOLLOWER_TO_LOYALTY_FAILED);
        }
    }

    private boolean registerToLoyalty(Follower follower) throws Exception {
        logger.info("++++++++++++++++++ register start ========+++++++++");
        if (StringUtils.isNotEmpty(follower.getUnionid())) {
            List<Follower> fs = followerDaoImpl.findAllByUnionid(follower.getUnionid());
            boolean noNeedSync = false;
            for (Follower f : fs) {
                noNeedSync = noNeedSync || f.isRecordToLoyaltySuccess();
            }
            if (noNeedSync) {
                return true;
            }
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        params.put("channel", "WeChat");
        params.put("name", follower.getNickname());
        String subscribeStatus = follower.getSubscribe() != 0 ? "true" : "false";
        params.put("custom_attributes[subscribe_status]", subscribeStatus);
        params.put("vendor", "wechat");
        params.put("vendor_id", follower.getOpenid());
        params.put("auto_enroll", "true");

        PrismResponse response = CustomerAPI.enroll(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()),
                params);

        // Map<String, String> addAttributes = new HashMap<String, String>();
        // addAttributes.put("op", "replace");
        // addAttributes.put("external_customer_id",
        // followerServiceImpl.getFollowerUnionId(follower.getOpenid()));
        // addAttributes.put("path", "/subscribe_status");
        // addAttributes.put("value", "subscribe");
        // CustomerAPI.customerUpdateAttributes(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()),
        // addAttributes);

        logger.info("+++++++==  ======= register response" + response);
        return response.toPrismData().isSuccess();
    }

    @Override
    public PrismResponse rewards(Long channelId, String filter) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(filter)) {
            params.put("filter", filter);
        }

        PrismResponse response = DataAPI.rewards(loyaltyConfigServiceImpl.getEnvByPbNoId(channelId), params);

        return response;
    }

    @Override
    public PrismResponse rewardGroups(Long channelId) throws Exception {

        PrismResponse response = DataAPI.rewardGroups(loyaltyConfigServiceImpl.getEnvByPbNoId(channelId));

        return response;
    }

    @Override
    public PrismResponse customerRewards(String openid, String filter) throws Exception {
        Follower follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        if (StringUtils.isNotEmpty(filter)) {
            params.put("filter", filter);
        }
        PrismResponse response = CustomerAPI
                .customerRewards(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response;
    }

    @Override
    public PrismResponse customerCoupons(String openid) throws Exception {
        Follower follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        PrismResponse response = CustomerAPI
                .customerCoupons(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response;
    }

    @Override
    public ResponseData customerShowBasic(String openid) throws Exception {
        Follower follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        // params.put("include",
        // "badges,coupons,offers,rewards,member_attributes,identities,reward_stats");
        PrismResponse response = CustomerAPI
                .customerShow(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response.toPrismData();
    }

    @Override
    public ResponseData customerShowRewards(String openid) throws Exception {
        Follower follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        params.put("include", "rewards,reward_stats");
        PrismResponse response = CustomerAPI
                .customerShow(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response.toPrismData();
    }

    @Override
    public PrismResponse customerEvents(LoyaltyEventsVo vo) throws Exception {
        Follower follower = followerServiceImpl.findOneByOpenid(vo.getId());
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        if (StringUtils.isNotEmpty(vo.getType())) {
            params.put("type", vo.getType());
        }
        params.put("page_number", vo.getPageNumber());
        params.put("entries_per_page", vo.getPageSize());

        PrismResponse response = CustomerAPI
                .customerEvents(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
        return response;
    }

    @Override
    public PrismResponse redeemReward(LoyaltyRewardsRedeemVo vo) throws Exception {
        Follower follower = followerServiceImpl.findOneByOpenid(vo.getId());
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        RewardsRedeemLog redeemLog = new RewardsRedeemLog();
        redeemLog.setOpenid(vo.getId());
        redeemLog.setPhone(vo.getPhone());
        redeemLog.setName(vo.getName());
        redeemLog.setAddress(vo.getAddress());
        redeemLog.setRewardId(vo.getRewardId());
        redeemLog.setRewardName(vo.getRewardName());
        redeemLog.setRewardType(vo.getRewardType());
        redeemLog.setPubNoAppId(follower.getPubNoAppId());
        redeemLog.setRewardGroup(vo.getRewardGroup());

        PrismResponse response = null;
        try {
            response = sendRedeemRewardsEvent(vo, follower);
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            redeemLog.setRedeemStatus(isSuccess);
            redeemLog.setRedeemResponse(response.toString());

            if (isSuccess) {
                JsonNode code = jsonNode.get("data").findValue("code");
                if (code != null) {
                    redeemLog.setCouponCode(code.asText());
                }
                redeemLog = weixinTemplateMessageServiceImpl.sendNoticeTemplateMessage(follower, redeemLog);
            }

            rewardsRedeemLogServciceImpl.save(redeemLog);
        } catch (Exception e) {
            redeemLog.setRedeemStatus(false);
            rewardsRedeemLogServciceImpl.save(redeemLog);
            logger.error(e.getMessage(), e);
            throw e;
        }

        return response;
    }

    @Override
    public PrismResponse recordSeeed(String openid, String type) {
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }
        logger.info("========= record seeed for " + openid + " type: " + type + "======");
        PrismResponse recordEvent = recordEvent(openid, follower.getPubNoAppId(), type);
        logger.info(
                "========= record seeed for " + openid + " type: " + type + " end resonse: " + recordEvent.toString());
        return recordEvent;

    }

    private PrismResponse sendRedeemRewardsEvent(LoyaltyRewardsRedeemVo vo, Follower follower) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", getUnionIdFromFollower(follower));
        params.put("reward_id", vo.getRewardId());

        return EventsAPI.redeemReward(loyaltyConfigServiceImpl.getEnvByAppId(follower.getPubNoAppId()), params);
    }

    @Override
    public ResponseData syncBindInfoToLoyalty(FollowerBindInfo bindInfo) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(bindInfo.getOpenid()));
        params.put("new_email", bindInfo.getEmail());
        params.put("mobile_phone", bindInfo.getPhone());
        params.put("address_line_1", bindInfo.getAddress());
        params.put("state", bindInfo.getProvince());
        params.put("city", bindInfo.getCity());
        params.put("first_name", bindInfo.getName());
        PrismResponse response = CustomerAPI
                .customerUpdateInfo(loyaltyConfigServiceImpl.getEnvByAppId(bindInfo.getAppId()), params);
        System.out.println("++++ sync bind info part1 ++" + response);

        params.clear();
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(bindInfo.getOpenid()));
        params.put("address_line_2", bindInfo.getDistrict());
        params.put("custom_attributes[digikey_customer_number]", bindInfo.getDigikeyCustomerNumber());
        params.put("custom_attributes[currency]", bindInfo.getCurrency());
        params.put("custom_attributes[career_title]", bindInfo.getTitle());
        params.put("custom_attributes[tencent_qq]", bindInfo.getQq());

        response = CustomerAPI.customerUpdateInfo(loyaltyConfigServiceImpl.getEnvByAppId(bindInfo.getAppId()), params);
        System.out.println("++++ sync bind info part2 ++" + response);
        return response.toPrismData();
    }

    @Override
    public ResponseData updateExternalCustomerId(String openid, String appId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("external_customer_id", openid);
        String unionid = followerServiceImpl.getFollowerUnionId(openid);
        if (StringUtils.isEmpty(unionid)) {
            throw new Exception("unionid is null");
        }
        params.put("new_external_customer_id", unionid);
        PrismResponse response = CustomerAPI.customerUpdateInfo(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        System.out.println("++++ update external_customer_id ++++" + response);
        return response.toPrismData();
    }

    @Override
    public ResponseData recordBindInfoEvent(String openid, String appId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "wechat-bind");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response.toPrismData();
    }

    @Override
    public void syncFollowerToLoyalty(WechatPublicNo pbNo) {
        List<Follower> followers = followerDaoImpl
                .findAllByPubNoAppIdAndRecordToLoyaltySuccess(pbNo.getAuthorizerAppid(), false);
        followers.forEach((f) -> {
            try {
                boolean success = registerToLoyalty(f);
                if (success) {
                    f.setRecordToLoyaltySuccess(success);
                    followerServiceImpl.updateFollower(f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public PrismResponse recordCompleteProfileEvent(String openid, String appId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "complete_profile");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByAppId(appId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public PrismResponse recordOfflineCheckinEvent(String openid, Long pbNoId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "offline_checkin");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByPbNoId(pbNoId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response;
    }

    @Override
    public String recordCommentBonusEvent(Long channelId, String openid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "comment-bonus");
        params.put("external_customer_id", followerServiceImpl.getFollowerUnionId(openid));
        params.put("channel", "WeChat");

        PrismResponse response = null;
        try {
            response = EventsAPI.record(loyaltyConfigServiceImpl.getEnvByPbNoId(channelId), params);
        } catch (Exception e) {
            throw new ServiceWarn("Loyalty Service Error");
        }
        System.out.println(response);
        return response.toPrismData().toString();
    }

    private String getUnionIdFromFollower(Follower follower) {
        // return follower.getOpenid();
        String unionId = StringUtils.isEmpty(follower.getUnionid()) ? follower.getOpenid() : follower.getUnionid();
        return unionId;
    }


    @Override
    public PrismResponse pointRules(Long channelId) throws Exception {
        PrismResponse response = DataAPI.pointGroups(loyaltyConfigServiceImpl.getEnvByPbNoId(channelId));

        return response;
    }

}
