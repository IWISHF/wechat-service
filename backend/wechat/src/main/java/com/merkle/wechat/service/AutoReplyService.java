package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.KeywordsAutoReply;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.autoreply.KeywordsAutoReplyRuleVo;

public interface AutoReplyService {

    void triggerKeywordsAutoReply(Long keywordsAutoReplyId, Long channelId, boolean enable) throws Exception;

    void delete(Long keywordsAutoReplyId, Long channelId) throws Exception;

    Pagination<KeywordsAutoReply> search(Long channelId, String key, Pageable pageable);

    void createKeywordsAutoReply(KeywordsAutoReply autoReply, WechatPublicNo pbNo) throws Exception;

    void updateKeywordsAutoReply(KeywordsAutoReply autoReply, Long keywordsId, Long channelId) throws Exception;

    void replyTextMessage(EventMessage message);

    void replyScanEvent(EventMessage event);

    void replySubscribeScanEvent(EventMessage event, boolean isNewFollower);

    List<KeywordsAutoReply> findActiveRulesByToUserName(String toUserName);

    void relpySubscribeEvent(EventMessage event, boolean isNewFollower);

    KeywordsAutoReplyRuleVo getKeywordsAutoReplyDetail(Long keywordsId, Long channelId);

    void replyClickEvent(EventMessage event);

}
