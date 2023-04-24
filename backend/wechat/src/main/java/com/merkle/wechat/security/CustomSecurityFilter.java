package com.merkle.wechat.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.merkle.wechat.util.JwtUtil;

/**
 * 
 * @author tyao
 *
 */
public class CustomSecurityFilter extends BasicAuthenticationFilter {
    private static final String X_AUTH_TOKEN = "x-auth-token";

    public CustomSecurityFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public CustomSecurityFilter(AuthenticationManager authenticationManager,
            AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Autowired
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String authToken = request.getHeader(X_AUTH_TOKEN);

            if (authToken != null) {
                CustomAuthenticationToken token = new CustomAuthenticationToken(authToken);
                // 放在这里不太好
                if (!request.getServletPath().contentEquals("/health")) {
                    UserInfo userInfo = JwtUtil.parseToken((String) token.getCredentials());
                    checkChannelId(request, userInfo.getPbNoIds());
                }

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

    private void checkChannelId(HttpServletRequest request, String pbNoIdStr) {
        Long channelId = 0L;
        String path = request.getServletPath();
        String[] split = path.split("/");
        try {
            channelId = Long.valueOf(split[1]);
            boolean correct = false;
            String[] pbNoIds = pbNoIdStr.split(",");
            for (String pbNoId : pbNoIds) {
                if (pbNoId.equals(channelId + "")) {
                    correct = true;
                }
            }
            if (!correct) {
                throw new AuthenticationServiceException("invalid token!");
            }
        } catch (NumberFormatException e) {
        }

    }
}
