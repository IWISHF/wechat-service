package com.merkle.wechat.cache;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.loyalty.LoyaltyConfig;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CacheLoyaltyConfigTemplate extends BaseCache<LoyaltyConfig> {

}
