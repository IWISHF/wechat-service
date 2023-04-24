package com.merkle.wechat.service;

public interface ThirdPartyService {

    String getThirdPartyAppId();

    String getThirdPartyAppSecret();
    
    String getFrontDomain();
    
    String getBackendDomain();

}
