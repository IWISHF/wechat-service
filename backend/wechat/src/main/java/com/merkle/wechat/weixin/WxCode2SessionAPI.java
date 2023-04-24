package com.merkle.wechat.weixin;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import weixin.popular.api.WxaAPI;
import weixin.popular.client.LocalHttpClient;

public class WxCode2SessionAPI extends WxaAPI {

    /**
     * code2Session<br>
     * 兑换sessionKey，登录凭证校验
     * 
     * @since 2.8.9
     * @param appId
     *            miniprogram appid
     * @param appSecret
     *            miniprogram secret
     * @param code
     *            jscode
     * @return result
     */
    public static Code2SessionResult code2Session(String appId, String appSecret, String code) {
        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setUri(BASE_URI + "/sns/jscode2session")
                .addParameter("grant_type", "authorization_code")
                .addParameter("appid", appId)
                .addParameter("secret", appSecret)
                .addParameter("js_code", code)
                .build();
        return LocalHttpClient.executeJsonResult(httpUriRequest, Code2SessionResult.class);
    }
}
