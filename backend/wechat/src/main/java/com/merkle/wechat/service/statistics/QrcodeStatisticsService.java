package com.merkle.wechat.service.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.Qrcode;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.statistics.QrcodeStatisticsItem;
import com.merkle.wechat.vo.statistics.QrcodeStatisticsVo;

public interface QrcodeStatisticsService {

    void create(EventMessage event, Qrcode qr, boolean isSubscribe);

    List<QrcodeStatisticsItem> statisticsByDate(String start, String end, Long qrcodeId);

    Pagination<QrcodeStatisticsVo> searchByName(WechatPublicNo pbNo, String key, Pageable pageable);

    Map<String, QrcodeStatisticsItem> statisticsByDateDetail(WechatPublicNo pbNo, String start, String end,
            Long qrcodeId);

}
