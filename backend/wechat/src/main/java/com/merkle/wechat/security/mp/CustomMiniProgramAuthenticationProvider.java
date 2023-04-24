package com.merkle.wechat.security.mp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.merkle.wechat.util.JwtMiniProgramUtil;

/**
 * 
 * @author tyao
 *
 */
public class CustomMiniProgramAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // decode token
        CustomMiniProgramAuthToken token = (CustomMiniProgramAuthToken) authentication;
        String openid = JwtMiniProgramUtil.parseToken((String) token.getCredentials());

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("mini"));
        // create token
        CustomMiniProgramAuthToken auth = new CustomMiniProgramAuthToken(openid, authorities);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (CustomMiniProgramAuthToken.class).isAssignableFrom(authentication);
    }

}
