package com.merkle.wechat.security.mp;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author tyao
 *
 */
public class CustomMiniProgramAuthToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -7507052706565107820L;
    private String authToken;
    private String openid;

    public CustomMiniProgramAuthToken(String authToken) {
        super(null);
        this.authToken = authToken;
        super.setAuthenticated(false);
    }

    public CustomMiniProgramAuthToken(String openid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openid = openid;
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
        return openid;
    }

    public String getUserInfo() {
        return openid;
    }

}
