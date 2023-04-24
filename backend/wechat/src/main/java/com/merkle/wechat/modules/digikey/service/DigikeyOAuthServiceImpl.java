package com.merkle.wechat.modules.digikey.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.merkle.wechat.cache.CacheTemplate;
import com.merkle.wechat.common.dao.RefreshFlagDao;
import com.merkle.wechat.common.dao.digikey.DigikeyTokenDao;
import com.merkle.wechat.common.entity.RefreshFlag;
import com.merkle.wechat.common.entity.digikey.DigikeyToken;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.Constants;
import com.merkle.wechat.constant.ExceptionConstants;

@Component
public class DigikeyOAuthServiceImpl {
    protected Logger logger = LoggerFactory.getLogger("DigikeyOAuthServiceImpl");

    @Value("${digikey.oauth.client.id}")
    private String clientId;

    @Value("${digikey.oauth.client.secret}")
    private String clientSecret;

    @Value("${thirdparty.backend.domain}")
    private String domain;

    private final String OAUTH_CODE_PATH = "/wechat/digikey/oauth/code";

    private @Autowired DigikeyTokenDao digikeyTokenDaoImpl;

    private @Autowired RefreshFlagDao tokenRefreshDaoImpl;

    private @Autowired CacheTemplate stringCacheTemplate;

    @Deprecated
    public void requestAuthCode() throws UnirestException {
        String requestPath = "https://sso.digikey.com/as/authorization.oauth2";
        String redirectUrl = domain + OAUTH_CODE_PATH;
        HttpResponse<String> result = Unirest.get(requestPath).queryString("response_type", "code")
                .queryString("client_id", clientId).queryString("redirect_uri", redirectUrl).asString();
        System.out.println(result);
    }

    public void createAccessToken(String code) throws UnirestException {
        String requestPath = "https://sso.digikey.com/as/token.oauth2";
        String redirectUrl = domain + OAUTH_CODE_PATH;
        HttpResponse<JsonNode> resp = Unirest.post(requestPath).field("code", code).field("client_id", clientId)
                .field("client_secret", clientSecret).field("redirect_uri", redirectUrl)
                .field("grant_type", "authorization_code").asJson();
        DigikeyToken token = Optional.ofNullable(digikeyTokenDaoImpl.findByClientId(clientId))
                .orElseGet(() -> new DigikeyToken());
        if (resp.getStatus() == 200) {
            JSONObject result = resp.getBody().getObject();
            token.setAccessToken(result.getString("access_token"));
            token.setClientId(clientId);
            token.setRefreshToken(result.getString("refresh_token"));
            token.setType(result.getString("token_type"));
            token.setExpiresIn(result.getLong("expires_in"));
            token.setCreatedDate(new Date());
            digikeyTokenDaoImpl.save(token);
        } else {
            throw new ServiceWarn(ExceptionConstants.UN_KNOWN_ERROR);
        }
        System.out.println(resp);
    }

    public String getDigikeyAccessToken() {
        String accessToken = stringCacheTemplate.get(clientId);

        if (StringUtils.isNotEmpty(accessToken)) {
            Long expireDate = Long.valueOf(stringCacheTemplate.get(clientId + Constants.EXPIRED_DATE_SUFFIX));
            if (System.currentTimeMillis() >= expireDate) {
                accessToken = syncDataBaseTokenToCache(clientId);
            }
        } else {
            accessToken = syncDataBaseTokenToCache(clientId);
        }
        return accessToken;
    }

    private String syncDataBaseTokenToCache(String appId) {
        DigikeyToken token = Optional.ofNullable(digikeyTokenDaoImpl.findByClientId(appId)).orElse(new DigikeyToken());
        String accessToken = token.getAccessToken();
        if (StringUtils.isNotEmpty(accessToken)) {
            putTokenToCache(appId, token);
        }
        return accessToken;
    }

    private void putTokenToCache(String appId, DigikeyToken token) {
        stringCacheTemplate.put(appId, token.getAccessToken());
        long expired = token.getCreatedDate().getTime() + 23 * 60 * 60 * 1000;
        stringCacheTemplate.put(appId + Constants.EXPIRED_DATE_SUFFIX, expired + "");
    }

    public void refreshAccessToken(DigikeyToken accessToken) throws UnirestException {
        String clientId = accessToken.getClientId();
        boolean isSuccess = createTokenRefreshFlag(clientId + "_digikey");
        if (!isSuccess) {
            return;
        }
        String requestPath = "https://sso.digikey.com/as/token.oauth2";
        HttpResponse<JsonNode> resp = Unirest.post(requestPath).field("client_id", clientId)
                .field("client_secret", clientSecret).field("refresh_token", accessToken.getRefreshToken())
                .field("grant_type", "refresh_token").asJson();
        if (resp.getStatus() == 200) {
            JSONObject result = resp.getBody().getObject();
            accessToken.setAccessToken(result.getString("access_token"));
            accessToken.setClientId(clientId);
            accessToken.setRefreshToken(result.getString("refresh_token"));
            accessToken.setType(result.getString("token_type"));
            accessToken.setCreatedDate(new Date());
            accessToken.setExpiresIn(result.getLong("expires_in"));
            digikeyTokenDaoImpl.save(accessToken);
            putTokenToCache(clientId, accessToken);
        }
        removeTokenRefreshFlag(accessToken.getClientId() + "_digikey");
    }

    public void refreshDigikeyTokens() throws UnirestException {
        logger.info("============= refresh Digikey Token Start ======");
        List<DigikeyToken> tokens = getNearExpireTokens();

        for (DigikeyToken token : tokens) {
            refreshAccessToken(token);
        }
        logger.info("============= refresh digikey Token end======");
    }

    public List<DigikeyToken> getNearExpireTokens() {
        long currentTimeMills = System.currentTimeMillis();
        // 23h
        currentTimeMills -= 23 * 60 * 60 * 1000;
        Date date = new Date(currentTimeMills);
        return digikeyTokenDaoImpl.findByCreatedDateLessThan(date);
    }

    public boolean createTokenRefreshFlag(String appId) {
        try {
            tokenRefreshDaoImpl.save(new RefreshFlag(appId));
        } catch (Exception e) {
            // 1 hour and 40 minutes ago
            // protect accessToken can be refresh successfully
            Date date = new Date(System.currentTimeMillis() - 600000);
            RefreshFlag token = tokenRefreshDaoImpl.findByAppIdAndCreatedDateLessThan(appId, date);
            if (null != token) {
                tokenRefreshDaoImpl.delete(token);
            }
            return false;
        }
        return true;
    }

    public void removeTokenRefreshFlag(String appId) {
        try {
            tokenRefreshDaoImpl.removeByAppId(appId);
        } catch (Exception e) {
            logger.info("delete refresh flag exception:" + e.getMessage());
        }
    }

}
