package com.merkle.wechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.config.ThirdPartyPlatformConfig;

@Component
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private @Autowired ThirdPartyPlatformConfig ttpConfig;

    @Override
    public String getThirdPartyAppId() {
        return ttpConfig.getThirdPartyAppId();
    }

    @Override
    public String getThirdPartyAppSecret() {
        return ttpConfig.getThirdPartyAppSecret();
    }

    @Override
    public String getFrontDomain() {
        return ttpConfig.getThirdPartyFrontDomain();
    }

    @Override
    public String getBackendDomain() {
        return ttpConfig.getThirdPartyBackendDomain();
    }

}
