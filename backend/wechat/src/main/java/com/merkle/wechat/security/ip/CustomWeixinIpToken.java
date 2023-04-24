package com.merkle.wechat.security.ip;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author tyao
 *
 */
public class CustomWeixinIpToken extends AbstractAuthenticationToken {

    private String x_forworded_for;

    public CustomWeixinIpToken() {
        super(null);
    }

    public CustomWeixinIpToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    private static final long serialVersionUID = -3054165596114991885L;

    @Override
    public Object getCredentials() {
        return "crediential";
    }

    @Override
    public Object getPrincipal() {
        return "test";
    }

    public String getX_forworded_for() {
        return x_forworded_for;
    }

    public void setX_forworded_for(String x_forworded_for) {
        this.x_forworded_for = x_forworded_for;
    }

}
