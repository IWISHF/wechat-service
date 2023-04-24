package com.merkle.wechat.handler;

import com.merkle.wechat.common.entity.EventMessage;

public interface MessageHandler {

    public default void handleTextMessage(EventMessage message) throws Exception {
    }

    public default void handleImageMessage(EventMessage message) throws Exception {
    }

    public default void handleVoiceMessage(EventMessage message) throws Exception {
    }

    public default void handleVideoMessage(EventMessage message) throws Exception {
    }

    public default void handleShortVideoMessage(EventMessage message) throws Exception {
    }

    public default void handleMsgLocationMessage(EventMessage message) throws Exception {
    }

    public default void handleLinkMessage(EventMessage message) throws Exception {
    }

    public default void handleSubscribeEvent(EventMessage event) throws Exception {
    }

    public default void handleUnSubscribeEvent(EventMessage event) throws Exception {
    }

    public default void handleEventLocationEvent(EventMessage event) throws Exception {
    }

    public default void handleClickEvent(EventMessage event) throws Exception {
    }

    public default void handleViewEvent(EventMessage event) throws Exception {
    }

    public default void handleScanEvent(EventMessage event) throws Exception {
    }

    public default void handleScanCodePushEvent(EventMessage event) throws Exception {
    }

    public default void handleScanCodeWaitingMsgEvent(EventMessage event) throws Exception {
    }

    public default void handleMessageJobFinished(EventMessage message) throws Exception {
    }

    public default boolean isSupport(EventMessage message) throws Exception {
        return true;
    }
}
