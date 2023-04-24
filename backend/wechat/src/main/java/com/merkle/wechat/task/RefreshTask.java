package com.merkle.wechat.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.merkle.wechat.modules.digikey.service.DigikeyOAuthServiceImpl;
import com.merkle.wechat.service.JsTicketService;
import com.merkle.wechat.service.TokenServiceImpl;
import com.merkle.wechat.service.batch.BatchTaskService;

@Component
@ConditionalOnProperty(prefix = "merkle.loyalty.refresh", name = "enable")
public class RefreshTask {
    protected Logger logger = LoggerFactory.getLogger("RefreshTokenTask");

    private @Autowired TokenServiceImpl tokenServiceImpl;

    private @Autowired JsTicketService jsTicketServiceImpl;

    private @Autowired BatchTaskService batchTaskServiceImpl;

    private @Autowired DigikeyOAuthServiceImpl digikeyOauthServiceImpl;

    // 启动10分钟后执行
    @Scheduled(fixedDelay = 300000, initialDelay = 600000)
    public void refreshToken() {
        tokenServiceImpl.refreshTokens();
    }

    // 启动10分钟后执行
    @Scheduled(fixedDelay = 300000, initialDelay = 600000)
    public void refreshDigikeyToken() throws UnirestException {
        digikeyOauthServiceImpl.refreshDigikeyTokens();
    }

    // 启动10分钟后执行
    @Scheduled(fixedDelay = 350000, initialDelay = 600000)
    public void refreshTicket() {
        jsTicketServiceImpl.refreshJsTickets();
    }

    // 启动10分钟后执行
    @Scheduled(fixedDelay = 300000, initialDelay = 600000)
    public void pollingRunBatchTask() {
        batchTaskServiceImpl.pollingRunBatchTask();
    }

}
