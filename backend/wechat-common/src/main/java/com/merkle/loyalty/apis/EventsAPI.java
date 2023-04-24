package com.merkle.loyalty.apis;

import java.util.Map;

import com.merkle.loyalty.PrismEnv;
import com.merkle.loyalty.config.PathConfig;
import com.merkle.loyalty.response.PrismResponse;

/**
 * 
 * @author tyao
 *
 */
public class EventsAPI extends BaseAPI {

    public static PrismResponse record(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.RECORD_EVENTS, params);
    }

    public static PrismResponse redeemReward(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.REWARD_REDEEM, params);
    }
}
