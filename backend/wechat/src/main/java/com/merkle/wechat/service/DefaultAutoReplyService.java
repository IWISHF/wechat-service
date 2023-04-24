package com.merkle.wechat.service;

import java.util.List;

import com.merkle.wechat.common.entity.DefaultAutoReply;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.autoreply.DefaultAutoReplyVo;

public interface DefaultAutoReplyService {

    void initDefaultRule(WechatPublicNo pbNo);

    void triggerDefaultAutoReply(Long defaultRuleId, Long channelId, boolean enable);

    void updateDefaultAutoReply(DefaultAutoReply autoReply, Long defaultRuleId, Long channelId) throws Exception;

    List<DefaultAutoReply> getDefaultAutoReplys(Long channelId);

    DefaultAutoReplyVo getDefaultAutoReplyDetail(Long defaultAutoReplyId, Long channelId);

}
