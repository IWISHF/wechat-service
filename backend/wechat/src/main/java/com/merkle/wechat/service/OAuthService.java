package com.merkle.wechat.service;

import com.merkle.wechat.vo.thridparty.JsTicketConfigVo;

public interface OAuthService {

    String generateOauthUrl(Long channelId, String redirect) throws Exception;

    String generateRedirectUrl(String base64path, String code, String state);

    JsTicketConfigVo generateJsTicketConfig(Long channelId, String url);

}
