package com.merkle.wechat.config;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.merkle.wechat.aop.ResultWrapperHandlerMethodReturnValueHandler;

/**
 * 
 * @author tyao
 *
 */
@Configuration
@EnableJpaAuditing
public class CustomConfig {

    @Value("${custom.config.serverTimeZone}")
    private String serverTimeZone;

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    /*
     * init the server after dependency injected
     */
    @PostConstruct
    public void init() {
        setReturnValueHandler();
        setDefaultTimeZone();
    }

    /*
     * set return value handler and change the order
     */
    public void setReturnValueHandler() {
        List<HandlerMethodReturnValueHandler> defaultResolvers = new ArrayList<>(adapter.getReturnValueHandlers());
        List<HandlerMethodReturnValueHandler> customResolvers = adapter.getCustomReturnValueHandlers();
        defaultResolvers.removeAll(customResolvers);

        // add ReturnValue Resolver
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(jackson2HttpMessageConverter);
        customResolvers.add(new ResultWrapperHandlerMethodReturnValueHandler(messageConverters));

        defaultResolvers.addAll(0, customResolvers);
        adapter.setReturnValueHandlers(defaultResolvers);
    }

    /*
     * If serverTimeZone is set will use server timeZone else use GMT timeZone
     */
    private void setDefaultTimeZone() {
        boolean zoneIsSet = null != serverTimeZone && !serverTimeZone.isEmpty();
        String zoneId = zoneIsSet ? serverTimeZone : "GMT";
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        TimeZone.setDefault(timeZone);
    }

}
