package com.merkle.wechat.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CsrfFilter;

import com.merkle.wechat.security.CustomAuthentiactionProvider;
import com.merkle.wechat.security.CustomEntryPoint;
import com.merkle.wechat.security.CustomSecurityFilter;

/**
 *
 * @author tyao
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * If you want just SA can access one api, you need
     * add @PreAuthorize("hasAuthority('SA')") to the consistent controller
     * method.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new CustomSecurityFilter(createCustomAuthenticationManager(), new CustomEntryPoint()),
                CsrfFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers(HttpMethod.GET, "/wechat/*/management/asset/image/get/*", "/wechat/*/management/follower/export/file")
                .permitAll()
                .antMatchers("/wechat/*/oauth", "/wechat/oauth/code", "/wechat/*/jssdk", "/wechat/platform/**", "/wechat/auth/*/*", "/health", "/wechat/user/login", "/wechat/api/aia/**")
                .permitAll()
                .antMatchers("/wechat/*/management/template/send", "/wechat/digikey/video/**", "/wechat/digikey/wish/**", "/wechat/*/survey/answer/**", "/wechat/*/survey/*", "/wechat/*/management/material/mpnews/comment/sync", "/wechat/api/follower/**", "/wechat/api/loyalty/**", "/wechat/*/campaign/**", "/wechat/follower/check/*", "/wechat/digikey/**", "/wechat/track/**", "/wechat/demo/**", "/wechat/loyalty/**", "/webjars/**", "/swagger-resources/**", "/csrf/**", "/v2/api-docs")
                .permitAll()
                .anyRequest()
                .authenticated().and().httpBasic();
        http.csrf().disable();
//        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public AuthenticationManager createCustomAuthenticationManager() throws Exception {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();
        authenticationProviders.add(new CustomAuthentiactionProvider());
        return new ProviderManager(authenticationProviders);
    }
}
