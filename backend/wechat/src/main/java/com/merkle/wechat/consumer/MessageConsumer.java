package com.merkle.wechat.consumer;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.handler.MessageHandler;

public interface MessageConsumer {
    public void consumeWechatMessage(EventMessage message) throws Exception;

    void registerHandler(MessageHandler messageHandler) throws Exception;

}
