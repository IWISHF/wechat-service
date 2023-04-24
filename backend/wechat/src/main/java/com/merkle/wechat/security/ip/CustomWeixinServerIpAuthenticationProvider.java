package com.merkle.wechat.security.ip;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.WeixinServerIpDao;

/**
 * 
 * @author tyao
 *
 */
public class CustomWeixinServerIpAuthenticationProvider implements AuthenticationProvider {
    Set<String> whitelist = new HashSet<String>();
    @SuppressWarnings("unused")
    private WeixinServerIpDao weixinServerIpDaoImpl;
    private String NOT_WEIXIN_IP = "ip is not in weixin server ip list";

    public CustomWeixinServerIpAuthenticationProvider(WeixinServerIpDao weixinServerIpDaoImpl) {
        this.weixinServerIpDaoImpl = weixinServerIpDaoImpl;
        weixinServerIpDaoImpl.findAll().forEach((data) -> {
            whitelist.add(data.getIp());
        });
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((CustomWeixinIpToken) authentication)
                .getDetails();
        String x_forwarded_for = ((CustomWeixinIpToken) authentication).getX_forworded_for();
        String userIp = details.getRemoteAddress();
        if (!StringUtils.isEmpty(x_forwarded_for)) {
            if (x_forwarded_for.contains(",")) {
                userIp = x_forwarded_for.split(",")[0];
            } else {
                userIp = x_forwarded_for;
            }
        }
        if (!whitelist.contains(userIp)) {
            throw new AuthenticationServiceException(NOT_WEIXIN_IP);
        }
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomWeixinIpToken.class.isAssignableFrom(authentication);
    }

}
