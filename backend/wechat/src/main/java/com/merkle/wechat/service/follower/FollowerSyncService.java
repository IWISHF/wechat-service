package com.merkle.wechat.service.follower;

public interface FollowerSyncService {

    void syncLatestFollowerInfoFromWechat(String appId) throws Exception;

}
