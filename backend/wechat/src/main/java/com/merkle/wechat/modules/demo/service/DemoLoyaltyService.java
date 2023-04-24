package com.merkle.wechat.modules.demo.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.merkle.loyalty.response.PrismResponse;
import com.merkle.wechat.common.entity.follower.Follower;

public interface DemoLoyaltyService {

    void syncFollowerToLoyalty(String openid, String appId) throws Exception;

    PrismResponse recordMenuClick(String id, String value, String publicNoAppId) throws Exception;

    PrismResponse recordSubscribe(String id, String publicNoAppId) throws Exception;

    PrismResponse recordPurchase(String id, String value, String refId, String eventId) throws Exception;

    PrismResponse redeem(String id, String value) throws ClientProtocolException, IOException, Exception;

    PrismResponse recordWXEvent(String type, String id, String publicNoAppId) throws Exception;

    PrismResponse recordCustomerEvent(String type, String id) throws Exception;

    PrismResponse getCustomerEvents(String id, String pageNumber, String pageSize) throws Exception;

    void unSubscribePublicNoUpate(String openid, String appId) throws Exception;

    void subscribePublicNoUpate(String openid, String appId) throws Exception;

    PrismResponse redeemReward(String id, String rewardId) throws Exception;

    PrismResponse getCustomerInfoFromLoyalty(String openid) throws Exception;

    void syncFollowerToLoyalty(Follower follower) throws Exception;

}
