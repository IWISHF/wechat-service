package com.merkle.wechat.consumer;

import static com.merkle.wechat.constant.Constants.CLICK;
import static com.merkle.wechat.constant.Constants.EVENT_LOCATION;
import static com.merkle.wechat.constant.Constants.IMAGE;
import static com.merkle.wechat.constant.Constants.LINK;
import static com.merkle.wechat.constant.Constants.MASSSEND_JOB_FINISH;
import static com.merkle.wechat.constant.Constants.MSG_LOCATION;
import static com.merkle.wechat.constant.Constants.SCAN;
import static com.merkle.wechat.constant.Constants.SCANCODE_PUSH;
import static com.merkle.wechat.constant.Constants.SCANCODE_WAITMSG;
import static com.merkle.wechat.constant.Constants.SHORT_VIDEO;
import static com.merkle.wechat.constant.Constants.SUBSCRIBE;
import static com.merkle.wechat.constant.Constants.TEXT;
import static com.merkle.wechat.constant.Constants.UNSUBSCRIBE;
import static com.merkle.wechat.constant.Constants.VIDEO;
import static com.merkle.wechat.constant.Constants.VIEW;
import static com.merkle.wechat.constant.Constants.VOICE;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.handler.DefaultMessageHandler;
import com.merkle.wechat.handler.MessageHandler;

@Component(value = "MessageConsumer")
public class MessageConsumerImpl implements MessageConsumer, MessageHandler {

    // Must set at index 0, it handle basic operations
    private @Autowired DefaultMessageHandler defaultMessageHandler;

    private List<MessageHandler> messageHandlers = new ArrayList<>();

    @PostConstruct
    public void init() throws Exception {
        if (!messageHandlers.contains(defaultMessageHandler)) {
            messageHandlers.add(0, defaultMessageHandler);
        }
    }

    @Override
    public void consumeWechatMessage(EventMessage message) throws Exception {
        if (message == null) {
            return;
        }
        String typeStr = message.getMsgType();
        if (typeStr.equals("event")) {
            typeStr = message.getEvent();
        }
        switch (typeStr) {
            /* Message type */
            case TEXT:
                handleTextMessage(message);
                break;
            case IMAGE:
                handleImageMessage(message);
                break;
            case VOICE:
                handleVoiceMessage(message);
                break;
            case VIDEO:
                handleVideoMessage(message);
                break;
            case SHORT_VIDEO:
                handleShortVideoMessage(message);
                break;

            case MSG_LOCATION:
                handleMsgLocationMessage(message);
                break;
            case LINK:
                handleLinkMessage(message);
                break;

            /* Event type */
            case SUBSCRIBE:
                handleSubscribeEvent(message);
                break;
            case UNSUBSCRIBE:
                handleUnSubscribeEvent(message);
                break;
            case EVENT_LOCATION:
                handleEventLocationEvent(message);
                break;
            case CLICK:
                handleClickEvent(message);
                break;
            case VIEW:
                handleViewEvent(message);
                break;
            case SCAN:
                handleScanEvent(message);
                break;
            case SCANCODE_PUSH:
                handleScanCodePushEvent(message);
                break;
            case SCANCODE_WAITMSG:
                handleScanCodeWaitingMsgEvent(message);
                break;
            case MASSSEND_JOB_FINISH:
                handleMessageJobFinished(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void registerHandler(MessageHandler messageHandler) throws Exception {
        if (!messageHandlers.contains(messageHandler)) {
            messageHandlers.add(messageHandler);
        }
    }

    @Override
    public void handleMessageJobFinished(EventMessage message) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(message))
                messageHandler.handleMessageJobFinished(message);
        }
    }

    @Override
    public void handleTextMessage(EventMessage message) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(message))
                messageHandler.handleTextMessage(message);
        }
    }

    @Override
    public void handleImageMessage(EventMessage message) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(message))
                messageHandler.handleImageMessage(message);
        }
    }

    @Override
    public void handleVoiceMessage(EventMessage message) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(message))
                messageHandler.handleVoiceMessage(message);
        }
    }

    @Override
    public void handleVideoMessage(EventMessage message) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(message))
                messageHandler.handleVideoMessage(message);
        }
    }

    @Override
    public void handleShortVideoMessage(EventMessage message) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(message))
                messageHandler.handleShortVideoMessage(message);
        }
    }

    @Override
    public void handleSubscribeEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleSubscribeEvent(event);
        }
    }

    @Override
    public void handleUnSubscribeEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleUnSubscribeEvent(event);
        }
    }

    @Override
    public void handleEventLocationEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleEventLocationEvent(event);
        }
    }

    @Override
    public void handleClickEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleClickEvent(event);
        }
    }

    @Override
    public void handleViewEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleViewEvent(event);
        }
    }

    @Override
    public void handleScanEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleScanEvent(event);
        }
    }

    @Override
    public void handleScanCodePushEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleScanCodePushEvent(event);
        }
    }

    @Override
    public void handleScanCodeWaitingMsgEvent(EventMessage event) throws Exception {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.isSupport(event))
                messageHandler.handleScanCodeWaitingMsgEvent(event);
        }
    }

}