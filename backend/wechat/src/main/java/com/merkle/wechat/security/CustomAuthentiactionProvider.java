package com.merkle.wechat.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.merkle.wechat.util.JwtUtil;

/**
 * 
 * @author tyao
 *
 */
public class CustomAuthentiactionProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // decode token
        CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;
        UserInfo userInfo = JwtUtil.parseToken((String) token.getCredentials());

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(userInfo.getRole()));
        // create token
        CustomAuthenticationToken auth = new CustomAuthenticationToken(userInfo, authorities);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (CustomAuthenticationToken.class).isAssignableFrom(authentication);
    }

}