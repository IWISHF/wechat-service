package com.merkle.wechat.service;

import com.merkle.wechat.common.entity.Token;
import com.merkle.wechat.common.entity.WechatPublicNo;

import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;

public interface TokenService {

    String getComponentAccessToken();

    void refreshAccessToken(Token accessToken);

    boolean createTokenRefreshFlag(String appId);

    void refreshComponentAccessToken();

    Token save(Token token);

    String getPublicNoAccessTokenByAppId(String appId);

    void createAccessToken(Authorization_info basicInfo);

    void refreshTokens();

    void createMiniProgramAccessToken(WechatPublicNo mini);

    void refreshMiniProgramAccessToken(Token miniToken);

}
