package com.merkle.wechat.service.loyalty;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.loyalty.PrismEnv;
import com.merkle.wechat.cache.CacheLoyaltyConfigTemplate;
import com.merkle.wechat.cache.CachePrismEnvTemplate;
import com.merkle.wechat.common.dao.loyalty.LoyaltyConfigDao;
import com.merkle.wechat.common.entity.loyalty.LoyaltyConfig;

@Component
public class LoyaltyConfigServiceImpl implements LoyaltyConfigService {
    private @Autowired LoyaltyConfigDao loyaltyConfigDaoImpl;
    private @Autowired CachePrismEnvTemplate cachePrismEnv;
    private @Autowired CacheLoyaltyConfigTemplate cacheLoyaltyConfig;

    @Override
    public PrismEnv getEnvByPbNoId(Long pbNoId) {
        PrismEnv env = Optional.ofNullable(cachePrismEnv.get(String.valueOf(pbNoId))).orElseGet(() -> {
            LoyaltyConfig config = getDBLoyaltyConfigByPbNoId(pbNoId);
            if (config == null) {
                return null;
            }
            PrismEnv prismEnv = new PrismEnv();
            prismEnv.setBaseUrl(config.getBaseUrl());
            prismEnv.setSecret(config.getSecret());
            prismEnv.setUuid(config.getUuid());
            cachePrismEnv.put(String.valueOf(pbNoId), prismEnv);
            cacheLoyaltyConfig.put(String.valueOf(pbNoId), config);
            return prismEnv;
        });
        return env;
    }

    @Override
    public PrismEnv getEnvByAppId(String appId) {
        PrismEnv env = Optional.ofNullable(cachePrismEnv.get(appId)).orElseGet(() -> {
            LoyaltyConfig config = getDBLoyaltyConfigByAppId(appId);
            if (config == null) {
                return null;
            }
            PrismEnv prismEnv = new PrismEnv();
            prismEnv.setBaseUrl(config.getBaseUrl());
            prismEnv.setSecret(config.getSecret());
            prismEnv.setUuid(config.getUuid());
            cachePrismEnv.put(appId, prismEnv);
            cacheLoyaltyConfig.put(appId, config);
            return prismEnv;
        });
        return env;
    }

    @Override
    public LoyaltyConfig getLoyaltyConfigByPbNoId(Long pbNoId) {
        LoyaltyConfig loyaltyConfig = Optional.ofNullable(cacheLoyaltyConfig.get(String.valueOf(pbNoId)))
                .orElseGet(() -> {
                    LoyaltyConfig config = getDBLoyaltyConfigByPbNoId(pbNoId);
                    if (config == null) {
                        return null;
                    }
                    PrismEnv prismEnv = new PrismEnv();
                    prismEnv.setBaseUrl(config.getBaseUrl());
                    prismEnv.setSecret(config.getSecret());
                    prismEnv.setUuid(config.getUuid());
                    cachePrismEnv.put(String.valueOf(pbNoId), prismEnv);
                    cacheLoyaltyConfig.put(String.valueOf(pbNoId), config);
                    return config;
                });
        return loyaltyConfig;
    }

    @Override
    public LoyaltyConfig getLoyaltyCosnfigByAppId(String appId) {
        LoyaltyConfig loyaltyConfig = Optional.ofNullable(cacheLoyaltyConfig.get(appId)).orElseGet(() -> {
            LoyaltyConfig config = getDBLoyaltyConfigByAppId(appId);
            if (config == null) {
                return null;
            }
            PrismEnv prismEnv = new PrismEnv();
            prismEnv.setBaseUrl(config.getBaseUrl());
            prismEnv.setSecret(config.getSecret());
            prismEnv.setUuid(config.getUuid());
            cachePrismEnv.put(appId, prismEnv);
            cacheLoyaltyConfig.put(appId, config);
            return config;
        });
        return loyaltyConfig;
    }

    public LoyaltyConfig getDBLoyaltyConfigByPbNoId(Long pbNoId) {
        return loyaltyConfigDaoImpl.findByWechatPublicNoId(pbNoId);
    }

    public LoyaltyConfig getDBLoyaltyConfigByAppId(String appId) {
        return loyaltyConfigDaoImpl.findByAppId(appId);
    }
}
