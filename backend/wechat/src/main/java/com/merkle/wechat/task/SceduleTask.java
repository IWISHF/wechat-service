package com.merkle.wechat.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.merkle.wechat.service.statistics.FollowerStatisticsService;

@Component
public class SceduleTask {
    private @Autowired FollowerStatisticsService followerStatisticsServiceImpl;

    // 每天23点
    @Scheduled(cron = "0 0 23 * * ? ")
    public void createTotalSubscribeFollowerStatistics() {
        followerStatisticsServiceImpl.createTotalFollowerStatistics();
    }
}
