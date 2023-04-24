package com.merkle.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @author tyao
 *
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.max-age}")
    private long maxAge;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedOrigins.split(",")).allowedMethods(allowedMethods.split(","))
                .allowCredentials(allowCredentials).maxAge(maxAge);
    }
}
