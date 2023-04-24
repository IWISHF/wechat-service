package com.merkle.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.merkle.wechat.vo.thridparty.ThirdPartyPlatformCryptArgs;

@Component
public class ThirdPartyPlatformConfig {
    @Value("${thirdparty.appId}")
    private String thirdPartyAppId;

    @Value("${thirdparty.appSecret}")
    private String thirdPartyAppSecret;

    @Value("${thirdparty.aeskey}")
    private String thirdPartyAesKey;

    @Value("${thirdparty.token}")
    private String thirdPartyToken;

    @Value("${thirdparty.front.domain}")
    private String thirdPartyFrontDomain;

    @Value("${thirdparty.backend.domain}")
    private String thirdPartyBackendDomain;

    public String getThirdPartyAppId() {
        return thirdPartyAppId;
    }

    public void setThirdPartyAppId(String thirdPartyAppId) {
        this.thirdPartyAppId = thirdPartyAppId;
    }

    public String getThirdPartyAppSecret() {
        return thirdPartyAppSecret;
    }

    public void setThirdPartyAppSecret(String thirdPartyAppSecret) {
        this.thirdPartyAppSecret = thirdPartyAppSecret;
    }

    public String getThirdPartyAesKey() {
        return thirdPartyAesKey;
    }

    public void setThirdPartyAesKey(String thirdPartyAesKey) {
        this.thirdPartyAesKey = thirdPartyAesKey;
    }

    public String getThirdPartyToken() {
        return thirdPartyToken;
    }

    public void setThirdPartyToken(String thirdPartyToken) {
        this.thirdPartyToken = thirdPartyToken;
    }

    public String getThirdPartyFrontDomain() {
        return thirdPartyFrontDomain;
    }

    public void setThirdPartyFrontDomain(String thirdPartyFrontDomain) {
        this.thirdPartyFrontDomain = thirdPartyFrontDomain;
    }

    public String getThirdPartyBackendDomain() {
        return thirdPartyBackendDomain;
    }

    public void setThirdPartyBackendDomain(String thirdPartyBackendDomain) {
        this.thirdPartyBackendDomain = thirdPartyBackendDomain;
    }

    public ThirdPartyPlatformCryptArgs getCryptArgs() {
        ThirdPartyPlatformCryptArgs args = new ThirdPartyPlatformCryptArgs();
        args.setEncryptAppId(thirdPartyAppId);
        args.setSecret(thirdPartyAppSecret);
        args.setEncodingAESKey(thirdPartyAesKey);
        args.setToken(thirdPartyToken);
        return args;
    }

}
