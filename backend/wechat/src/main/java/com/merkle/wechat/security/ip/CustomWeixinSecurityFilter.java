package com.merkle.wechat.security.ip;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class CustomWeixinSecurityFilter extends BasicAuthenticationFilter {
    public CustomWeixinSecurityFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public CustomWeixinSecurityFilter(AuthenticationManager authenticationManager,
            AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Autowired
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            CustomWeixinIpToken token = new CustomWeixinIpToken();
            token.setX_forworded_for(request.getHeader("x-forwarded-for"));
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = this.getAuthenticationManager().authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException failed) {
            this.getAuthenticationEntryPoint().commence(request, response, failed);
            return;
        }

        chain.doFilter(request, response);
    }
}
