package com.merkle.wechat.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

import com.merkle.wechat.common.dao.WeixinServerIpDao;
import com.merkle.wechat.security.CustomEntryPoint;
import com.merkle.wechat.security.ip.CustomWeixinSecurityFilter;
import com.merkle.wechat.security.ip.CustomWeixinServerIpAuthenticationProvider;

/**
 * 
 * @author tyao
 *
 */
@Order(1)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "spring.profiles.active", name = "notprod")
public class CustomWeixinServerIpSecurityConfig extends WebSecurityConfigurerAdapter {
    private @Autowired WeixinServerIpDao weixinServerIpDaoImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().anonymous().disable().httpBasic().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterAfter(
                        new CustomWeixinSecurityFilter(createCustomAuthenticationManager(), new CustomEntryPoint()),
                        CsrfFilter.class)
                .antMatcher("/wechat/platform/**").authorizeRequests().anyRequest().authenticated();
    }

    public AuthenticationManager createCustomAuthenticationManager() throws Exception {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();
        authenticationProviders.add(new CustomWeixinServerIpAuthenticationProvider(weixinServerIpDaoImpl));
        return new ProviderManager(authenticationProviders);
    }
}
