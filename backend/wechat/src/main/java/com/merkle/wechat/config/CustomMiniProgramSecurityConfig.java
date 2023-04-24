package com.merkle.wechat.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CsrfFilter;

import com.merkle.wechat.security.CustomEntryPoint;
import com.merkle.wechat.security.mp.CustomMiniProgramAuthenticationProvider;
import com.merkle.wechat.security.mp.CustomMiniProgramSecurityFilter;

/**
 * 
 * @author tyao
 *
 */
@Order(3)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomMiniProgramSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf()
        .disable()
        .httpBasic()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterAfter(
                new CustomMiniProgramSecurityFilter(createCustomAuthenticationManager(),new CustomEntryPoint()),
                CsrfFilter.class)
        .antMatcher("/wechat/mini/**").authorizeRequests().anyRequest().permitAll();
    }

    public AuthenticationManager createCustomAuthenticationManager() throws Exception {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();
        authenticationProviders.add(new CustomMiniProgramAuthenticationProvider());
        return new ProviderManager(authenticationProviders);
    }
}
