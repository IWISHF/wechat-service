package com.merkle.wechat.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author tyao
 *
 */
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -7507052706565107820L;
    private String authToken;
    private UserInfo userInfo;

    public CustomAuthenticationToken(String authToken) {
        super(null);
        this.authToken = authToken;
        super.setAuthenticated(false);
    }

    public CustomAuthenticationToken(UserInfo userInfo, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userInfo = userInfo;
        super.setAuthenticated(true);
    }

    /**
     * return authToken
     */
    @Override
    public Object getCredentials() {
        return authToken;
    }

    /**
     * return userId
     */
    @Override
    public Object getPrincipal() {
        return userInfo.getUserId();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

}
