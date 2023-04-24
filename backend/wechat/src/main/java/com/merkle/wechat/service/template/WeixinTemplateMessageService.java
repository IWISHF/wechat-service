package com.merkle.wechat.service.template;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.template.WeixinTemplate;
import com.merkle.wechat.vo.Pagination;

public interface WeixinTemplateMessageService {

    void syncAddedTemplate(Long pbNoId) throws Exception;

    Pagination<WeixinTemplate> search(Long pbNoId, String key, Pageable pageable);

    List<WeixinTemplate> getAllActive(Long pbNoId, String key);

    RewardsRedeemLog sendNoticeTemplateMessage(Follower follower, RewardsRedeemLog redeemLog);

    void sendExpressTemplateMessage(String trackingCode, Long logid, Long channelId) throws Exception;

}
