package com.merkle.wechat.service.statistics;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.statistics.TagStatisticsVo;

public interface TagStatisticsService {

    TagStatisticsVo searchTagByName(WechatPublicNo pbNo, String key, int fromWechat, Pageable pageable);

}
