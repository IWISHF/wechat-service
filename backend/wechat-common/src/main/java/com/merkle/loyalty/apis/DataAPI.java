package com.merkle.loyalty.apis;

import java.util.HashMap;
import java.util.Map;

import com.merkle.loyalty.PrismEnv;
import com.merkle.loyalty.config.PathConfig;
import com.merkle.loyalty.response.PrismResponse;

public class DataAPI extends BaseAPI {

    public static PrismResponse rewards(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.DATA_REWARDS, params);
    }

    public static PrismResponse rewardGroups(PrismEnv env) throws Exception {
        return send(env, PathConfig.DATA_REWARD_GROUPS, new HashMap<String, String>());
    }

    public static PrismResponse pointGroups(PrismEnv env) throws Exception {
        return send(env, PathConfig.DATA_POINT_RULE_GROUPS, new HashMap<String, String>());
    }
}
