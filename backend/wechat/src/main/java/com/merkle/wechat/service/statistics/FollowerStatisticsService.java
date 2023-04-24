package com.merkle.wechat.service.statistics;

import java.util.Map;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.statistics.FollowerStatisticsItem;
import com.merkle.wechat.vo.statistics.FollowerStatisticsVo;

public interface FollowerStatisticsService {

    void createSubscribe(EventMessage event) throws Exception;

    void createUnsubscribe(EventMessage event) throws Exception;

    Map<String, FollowerStatisticsItem> statisticsByDateDetail(WechatPublicNo pbNo, String start, String end, int type);

    FollowerStatisticsVo statisticsByDate(WechatPublicNo pbNo);

    void createTotalFollowerStatistics();

}
