package com.merkle.wechat.service;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.vo.thridparty.JsTicketConfigVo;

import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

@Component
public class OAuthServiceImpl implements OAuthService {

    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;

    private @Autowired ThirdPartyService ttpServiceImpl;

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired JsTicketService jsTicketServiceImpl;

    @Override
    public String generateOauthUrl(Long channelId, String redirect) throws Exception {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(channelId);
        if (pbNo == null) {
            throw new ServiceWarn("channel not exist");
        }
        // String redirectUri = ttpServiceImpl.getBackendDomain() +
        // "/wechat/oauth/code/"
        // + Base64Utils.encodeToString(redirect.getBytes());
        String redirectUri = ttpServiceImpl.getBackendDomain() + "/wechat/oauth/code?base64path="
                + Base64Utils.encodeToString(redirect.getBytes());
        
        String connectOauth2Authorize = SnsAPI.connectOauth2Authorize(pbNo.getAuthorizerAppid(), redirectUri, false,
                channelId + "", ttpServiceImpl.getThirdPartyAppId());

        return connectOauth2Authorize;
    }

    @Override
    public String generateRedirectUrl(String base64path, String code, String state) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(Long.valueOf(state));
        if (pbNo == null) {
            throw new ServiceWarn("channel not exist");
        }
        SnsToken snsToken = SnsAPI.oauth2ComponentAccessToken(pbNo.getAuthorizerAppid(), code,
                ttpServiceImpl.getThirdPartyAppId(), tokenServiceImpl.getComponentAccessToken());
        String path = new String(Base64Utils.decodeFromString(base64path));
        if (path.contains("?")) {
            path += "&openid=" + snsToken.getOpenid();
        } else {
            path += "?openid=" + snsToken.getOpenid();
        }
        return path + "&channel=" + state;
    }

    @Override
    public JsTicketConfigVo generateJsTicketConfig(Long channelId, String url) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(channelId);
        if (pbNo == null) {
            throw new ServiceWarn("channel not exist");
        }
        String appId = pbNo.getAuthorizerAppid();
        return sign(appId, jsTicketServiceImpl.getJsTicket(appId), url);
    }

    private JsTicketConfigVo sign(String appId, String jsTicket, String url) {
        String nonceStr = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        string1 = "jsapi_ticket=" + jsTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        System.out.println(string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsTicketConfigVo vo = new JsTicketConfigVo();
        vo.setUrl(url);
        vo.setAppId(appId);
        vo.setNonceStr(nonceStr);
        vo.setTimeStamp(timestamp);
        vo.setSignature(signature);

        return vo;
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
