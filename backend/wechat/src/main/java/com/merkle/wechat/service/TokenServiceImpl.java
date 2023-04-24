package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.cache.CacheTemplate;
import com.merkle.wechat.common.dao.RefreshFlagDao;
import com.merkle.wechat.common.dao.TokenDao;
import com.merkle.wechat.common.dao.WechatPublicNoDao;
import com.merkle.wechat.common.entity.ComponentTicket;
import com.merkle.wechat.common.entity.RefreshFlag;
import com.merkle.wechat.common.entity.Token;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.Constants;

import weixin.popular.api.ComponentAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.component.AuthorizerAccessToken;
import weixin.popular.bean.component.ComponentAccessToken;

@Component
public class TokenServiceImpl implements TokenService {

    protected Logger logger = LoggerFactory.getLogger("TokenServiceImpl");

    private @Autowired TokenDao tokenDaoImpl;

    private @Autowired ThirdPartyService ttpServiceImpl;

    private @Autowired RefreshFlagDao tokenRefreshDaoImpl;

    private @Autowired ComponentTicketService componentTicketServiceImpl;

    private @Autowired CacheTemplate stringCacheTemplate;

    private @Autowired WechatPublicNoDao wechatPublicNoDaoImpl;

    @Override
    public String getComponentAccessToken() {
        String accessToken = getAccessTokenByAppId(ttpServiceImpl.getThirdPartyAppId());
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        } else {
            Token token = createDefaultComponentToken();
            return token.getAccessToken();
        }
    }

    @Override
    public void createMiniProgramAccessToken(WechatPublicNo mini) {
        Token token = tokenDaoImpl.findByAppId(mini.getAuthorizerAppid());
        if (token == null) {
            token = new Token();
        }
        weixin.popular.bean.token.Token wxtoken = TokenAPI.token(mini.getAuthorizerAppid(), mini.getMiniAppSecret());
        if (wxtoken.isSuccess()) {
            token.setAccessToken(wxtoken.getAccess_token());
            token.setAppId(mini.getAuthorizerAppid());
            token.setType(Token.MINI_ACCESS_TOKEN);
            token.setCreatedDate(new Date());
            tokenDaoImpl.save(token);
        } else {
            throw new ServiceWarn(wxtoken.getErrmsg(), wxtoken.getErrcode());
        }
    }

    @Override
    public void createAccessToken(Authorization_info basicInfo) {
        Token token = tokenDaoImpl.findByAppId(basicInfo.getAuthorizer_appid());
        if (token == null) {
            token = new Token();
        }
        token.setAppId(basicInfo.getAuthorizer_appid());
        token.setAccessToken(basicInfo.getAuthorizer_access_token());
        token.setRefreshToken(basicInfo.getAuthorizer_refresh_token());
        token.setType(Token.PUBLIC_AUTH_TOKEN);
        tokenDaoImpl.save(token);
    }

    @Override
    public String getPublicNoAccessTokenByAppId(String appId) {
        return getAccessTokenByAppId(appId);
    }

    private String getAccessTokenByAppId(String appId) {
        String accessToken = stringCacheTemplate.get(appId);

        if (StringUtils.isNotEmpty(accessToken)) {
            Long expireDate = Long.valueOf(stringCacheTemplate.get(appId + Constants.EXPIRED_DATE_SUFFIX));
            if (System.currentTimeMillis() >= expireDate) {
                accessToken = syncDataBaseTokenToCache(appId);
            }
        } else {
            accessToken = syncDataBaseTokenToCache(appId);
        }
        return accessToken;
    }

    private String syncDataBaseTokenToCache(String appId) {
        Token token = Optional.ofNullable(tokenDaoImpl.findByAppId(appId)).orElse(new Token());
        String accessToken = token.getAccessToken();
        if (StringUtils.isNotEmpty(accessToken)) {
            putTokenToCache(appId, token);
        }
        return accessToken;
    }

    private void putTokenToCache(String appId, Token token) {
        stringCacheTemplate.put(appId, token.getAccessToken());
        long expired = token.getCreatedDate().getTime() + 88 * 60 * 1000;
        stringCacheTemplate.put(appId + Constants.EXPIRED_DATE_SUFFIX, expired + "");
    }

    @Override
    public Token save(Token token) {
        return tokenDaoImpl.save(token);
    }

    private Token createDefaultComponentToken() {
        ComponentTicket ticket = componentTicketServiceImpl.findOneByAppId(ttpServiceImpl.getThirdPartyAppId());
        ComponentAccessToken catoken = ComponentAPI.api_component_token(ttpServiceImpl.getThirdPartyAppId(),
                ttpServiceImpl.getThirdPartyAppSecret(), ticket.getTicket());
        Token token = new Token();
        token.setAccessToken(catoken.getComponent_access_token());
        token.setType(Token.COMPONENT_ACCESS_TOKEN);
        token.setAppId(ttpServiceImpl.getThirdPartyAppId());
        token = save(token);
        return token;
    }

    @Override
    public void refreshComponentAccessToken() {
        String appId = ttpServiceImpl.getThirdPartyAppId();
        boolean isSuccess = createTokenRefreshFlag(appId);
        if (!isSuccess) {
            return;
        }
        Token token = tokenDaoImpl.findByAppId(appId);
        ComponentTicket ticket = componentTicketServiceImpl.findOneByAppId(appId);
        ComponentAccessToken catoken = ComponentAPI.api_component_token(appId, ttpServiceImpl.getThirdPartyAppSecret(),
                ticket.getTicket());

        if (catoken.isSuccess()) {
            token.setAccessToken(catoken.getComponent_access_token());
            token.setCreatedDate(new Date());
            tokenDaoImpl.save(token);
            putTokenToCache(appId, token);
        }
        removeTokenRefreshFlag(appId);
    }

    @Override
    public void refreshMiniProgramAccessToken(Token miniToken) {
        WechatPublicNo mini = wechatPublicNoDaoImpl.findOneByAuthorizerAppid(miniToken.getAppId());

        Token token = tokenDaoImpl.findByAppId(mini.getAuthorizerAppid());
        if (token == null) {
            token = new Token();
        }
        weixin.popular.bean.token.Token wxtoken = TokenAPI.token(mini.getAuthorizerAppid(), mini.getMiniAppSecret());
        if (wxtoken.isSuccess()) {
            token.setAccessToken(wxtoken.getAccess_token());
            token.setAppId(mini.getAuthorizerAppid());
            token.setType(Token.MINI_ACCESS_TOKEN);
            token.setCreatedDate(new Date());
            tokenDaoImpl.save(token);
        } else {
            throw new ServiceWarn(wxtoken.getErrmsg(), wxtoken.getErrcode());
        }
    }

    @Override
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

    @Override
    public void refreshAccessToken(Token accessToken) {
        String appId = accessToken.getAppId();
        boolean isSuccess = createTokenRefreshFlag(appId);
        if (!isSuccess) {
            return;
        }
        Token token = tokenDaoImpl.findByAppId(ttpServiceImpl.getThirdPartyAppId());
        AuthorizerAccessToken api_authorizer_token = ComponentAPI.api_authorizer_token(token.getAccessToken(),
                ttpServiceImpl.getThirdPartyAppId(), appId, accessToken.getRefreshToken());
        if (api_authorizer_token.isSuccess()) {
            accessToken.setAccessToken(api_authorizer_token.getAuthorizer_access_token());
            accessToken.setRefreshToken(api_authorizer_token.getAuthorizer_refresh_token());
            accessToken.setCreatedDate(new Date());
            tokenDaoImpl.save(accessToken);
            putTokenToCache(appId, accessToken);
        }
        removeTokenRefreshFlag(accessToken.getAppId());
    }

    @Override
    public void refreshTokens() {
        // TODO: 并发控制是否可以优化
        logger.info("============= refresh Token Start ======");
        List<Token> tokens = getNearExpireTokens();

        for (Token token : tokens) {
            if (token.getType().equals(Token.COMPONENT_ACCESS_TOKEN)) {
                refreshComponentAccessToken();
            }

            if (token.getType().equals(Token.PUBLIC_AUTH_TOKEN)) {
                refreshAccessToken(token);
            }

            if (token.getType().equals(Token.MINI_ACCESS_TOKEN)) {
                refreshMiniProgramAccessToken(token);
            }
        }
        logger.info("============= refresh Token end======");
    }

    public List<Token> getNearExpireTokens(Date date) {
        return tokenDaoImpl.findByCreatedDateLessThan(date);
    }

    public List<Token> getNearExpireTokens() {
        long currentTimeMills = System.currentTimeMillis();
        // 1 h 30 minutes
        currentTimeMills -= 90 * 60 * 1000;
        Date date = new Date(currentTimeMills);
        return tokenDaoImpl.findByCreatedDateLessThan(date);
    }

}
