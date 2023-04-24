package com.merkle.wechat.service;

public interface ComponentService {

    String buildAuthUrl(Long accountId);

    void syncPublicNoInfo(String auth_code, Long accountId) throws Exception;

    void deAuth(String appId);

}
