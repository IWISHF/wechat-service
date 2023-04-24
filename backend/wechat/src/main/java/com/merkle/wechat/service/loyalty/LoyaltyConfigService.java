package com.merkle.wechat.service.loyalty;

import com.merkle.loyalty.PrismEnv;
import com.merkle.wechat.common.entity.loyalty.LoyaltyConfig;

public interface LoyaltyConfigService {

    PrismEnv getEnvByPbNoId(Long pbNoId);

    PrismEnv getEnvByAppId(String appId);

    LoyaltyConfig getLoyaltyConfigByPbNoId(Long pbNoId);

    LoyaltyConfig getLoyaltyCosnfigByAppId(String appId);

}
