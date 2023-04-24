package com.merkle.wechat.cache;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.merkle.loyalty.PrismEnv;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CachePrismEnvTemplate extends BaseCache<PrismEnv> {

}
