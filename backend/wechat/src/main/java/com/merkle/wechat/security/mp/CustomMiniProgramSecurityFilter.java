package com.merkle.wechat.security.mp;

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

/**
 * 
 * @author tyao
 *
 */
public class CustomMiniProgramSecurityFilter extends BasicAuthenticationFilter {

    private static final String X_AUTH_MINI_TOKEN = "x-auth-mini-token";

    public CustomMiniProgramSecurityFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public CustomMiniProgramSecurityFilter(AuthenticationManager authenticationManager,
            AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Autowired
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String authToken = request.getHeader(X_AUTH_MINI_TOKEN);

            if (authToken != null) {
                CustomMiniProgramAuthToken token = new CustomMiniProgramAuthToken(authToken);
                token.setDetails(new WebAuthenticationDetails(request));
                Authentication authentication = this.getAuthenticationManager().authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                chain.doFilter(request, response);
                return;
            }
        } catch (AuthenticationException failed) {
            this.getAuthenticationEntryPoint().commence(request, response, failed);
            return;
        }

        chain.doFilter(request, response);
    }

}
