package com.merkle.wechat.service;

import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.WechatPublicNo;

public interface PreviewQrcodeService {

    String createPreviewBatchTask(WechatPublicNo pbNo, AutoReplyRule rule);

    void replySubscribeEvent(EventMessage event);

    void replyScanEvent(EventMessage event);

}
