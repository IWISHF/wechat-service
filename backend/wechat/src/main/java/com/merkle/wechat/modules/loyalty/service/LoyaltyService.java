package com.merkle.wechat.modules.loyalty.service;

import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.digikey.DigikeyEventMultiChannel;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.tracking.TrackCampaignPageEvent;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyEventsVo;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyRewardsRedeemVo;

public interface LoyaltyService {

    PrismResponse recordWXEvent(String type, EventMessage event);

    PrismResponse recordTextEvent(EventMessage message);

    PrismResponse recordLinkEvent(EventMessage message);

    PrismResponse recordMenuClickEvent(EventMessage event);

    PrismResponse recordScanEvent(EventMessage event);

    PrismResponse recordMenuViewEvent(EventMessage event);

    void syncFollowerToLoyalty(String openid, String appId) throws Exception;

    PrismResponse recordUnsubscribeEvent(EventMessage event);

    PrismResponse recordDigikeyEvent(DigikeyEventMultiChannel event);

    PrismResponse rewards(Long channelId, String filter) throws Exception;

    PrismResponse rewardGroups(Long channelId) throws Exception;

    PrismResponse customerRewards(String openid, String filter) throws Exception;

    ResponseData customerShowBasic(String openid) throws Exception;

    ResponseData customerShowRewards(String openid) throws Exception;

    PrismResponse customerEvents(LoyaltyEventsVo vo) throws Exception;

    PrismResponse redeemReward(LoyaltyRewardsRedeemVo vo) throws Exception;

    PrismResponse recordCPE(TrackCampaignPageEvent event);

    ResponseData syncBindInfoToLoyalty(FollowerBindInfo bindInfo) throws Exception;

    ResponseData recordBindInfoEvent(String openid, String appId) throws Exception;

    void syncFollowerToLoyalty(WechatPublicNo pbNo);

    PrismResponse customerCoupons(String id) throws Exception;

    PrismResponse recordCompleteProfileEvent(String openid, String appId);

    PrismResponse recordOfflineCheckinEvent(String openid, Long pbNoId);

    String recordCommentBonusEvent(Long channelId, String openid);

    PrismResponse recordSeeed(String id, String string);

    PrismResponse recordEvent(String openid, String appId, String type);

    PrismResponse recordEvent(String openid, String appId, String type, String value);

    PrismResponse recordEvent(String openid, String appId, String type, String value, String detail);

    ResponseData updateExternalCustomerId(String openid, String appId) throws Exception;

    PrismResponse pointRules(Long channelId) throws Exception;

}
