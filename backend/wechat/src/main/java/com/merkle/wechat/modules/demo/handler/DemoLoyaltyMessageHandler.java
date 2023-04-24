package com.merkle.wechat.modules.demo.handler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.consumer.MessageConsumer;
import com.merkle.wechat.handler.MessageHandler;
import com.merkle.wechat.modules.demo.service.DemoLoyaltyServiceImpl;
import com.merkle.wechat.modules.demo.service.DemoTagService;
import com.merkle.wechat.service.TokenService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

@Component(value = "LoyaltyMessageHandler")
@DependsOn(value = "MessageConsumer")
@ConditionalOnProperty(prefix = "merkle.loyalty.demo", name = "enable")
public class DemoLoyaltyMessageHandler implements MessageHandler {

    protected Logger logger = LoggerFactory.getLogger("LoyaltyMessageHandler");

    @Autowired(required = true)
    @Qualifier("MessageConsumer")
    private MessageConsumer messageConsumer;

    private @Autowired DemoLoyaltyServiceImpl loyaltyServiceImpl;

    private @Autowired DemoTagService tagServiceImpl;
    private @Autowired TokenService tokenServiceImpl;

    @PostConstruct
    public void register() throws Exception {
        logger.info("=========== register loyalty message handler =========");
        messageConsumer.registerHandler(this);
    }

    public void handleTextMessage(EventMessage message) throws Exception {
        if (message.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || message.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            AsyncUtil.asyncRun(() -> {
                wxTextEventCallLoyalty(loyaltyServiceImpl, message.getFromUserName(), message.getAppId());
            });
        }

    };

    public void handleLinkMessage(EventMessage message) throws Exception {
        if (message.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || message.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            AsyncUtil.asyncRun(() -> {
                wxEventCallLoyalty(loyaltyServiceImpl, message.getFromUserName(), message.getAppId(), "wechat-link");
            });
        }
    };

    public void handleSubscribeEvent(EventMessage event) throws Exception {
        if (event.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || event.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            if (event.getEventKey() != null && event.getEventKey().contains("addDemoTag")) {
                tagServiceImpl.tagFollower(event.getFromUserName(), "demo");
                Message message = new TextMessage(event.getFromUserName(),
                        "Greetings, this is Merkle LoyaltyPlus demo.");
                MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), message);
            }
            AsyncUtil.asyncRun(() -> {
                register(loyaltyServiceImpl, event.getFromUserName(), event.getAppId());
            });
        }
    };

    public void handleUnSubscribeEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            wxEventCallLoyalty(loyaltyServiceImpl, event.getFromUserName(), event.getAppId(), "wechat-unsubscribe");
        });
    };

    public void handleClickEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            wxMenuClickCallLoyalty(loyaltyServiceImpl, event.getFromUserName(), event.getAppId(), "0");
        });
    };

    public void handleScanEvent(EventMessage event) throws Exception {
        if (event.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || event.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            if (event.getEventKey() != null && event.getEventKey().equals("addDemoTag")) {
                tagServiceImpl.tagFollower(event.getFromUserName(), "demo");
                Message message = new TextMessage(event.getFromUserName(),
                        "Greetings, this is Merkle LoyaltyPlus demo.");
                MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), message);
            }
            AsyncUtil.asyncRun(() -> {
                wxEventCallLoyalty(loyaltyServiceImpl, event.getFromUserName(), event.getAppId(), "wechat-scan");
            });
        }
    };

    public void handleScanCodePushEvent(EventMessage event) throws Exception {
        if (event.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || event.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            if (event.getEventKey() != null && event.getEventKey().equals("addDemoTag")) {
                tagServiceImpl.tagFollower(event.getFromUserName(), "demo");
            }
            AsyncUtil.asyncRun(() -> {
                wxEventCallLoyalty(loyaltyServiceImpl, event.getFromUserName(), event.getAppId(), "wechat-scan");
            });
        }
    };

    public void handleScanCodeWaitingMsgEvent(EventMessage event) throws Exception {
        if (event.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || event.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            if (event.getEventKey() != null && event.getEventKey().equals("addDemoTag")) {
                tagServiceImpl.tagFollower(event.getFromUserName(), "demo");
            }
            AsyncUtil.asyncRun(() -> {
                wxEventCallLoyalty(loyaltyServiceImpl, event.getFromUserName(), event.getAppId(), "wechat-scan");
            });
        }
    };

    private void wxTextEventCallLoyalty(DemoLoyaltyServiceImpl serviceImpl, String openId, String appId) {
        try {
            serviceImpl.recordWXEvent("wechat-text", openId, appId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void wxMenuClickCallLoyalty(DemoLoyaltyServiceImpl serviceImpl, String openid, String appId, String value) {
        try {
            serviceImpl.recordMenuClick(openid, "0", appId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void wxEventCallLoyalty(DemoLoyaltyServiceImpl serviceImpl, String openid, String appId, String type) {
        try {
            if (type.equalsIgnoreCase("wechat-unsubscribe")) {
                serviceImpl.unSubscribePublicNoUpate(openid, appId);
            }
            serviceImpl.recordWXEvent(type, openid, appId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void handleViewEvent(EventMessage event) throws Exception {
        if (event.getToUserName().equalsIgnoreCase("gh_3b7dd93e7c95")
                || event.getToUserName().equalsIgnoreCase("gh_bd4d470a1f97")) {
            AsyncUtil.asyncRun(() -> {
                wxMenuClickCallLoyalty(loyaltyServiceImpl, event.getFromUserName(), event.getAppId(), "0");
            });
        }
    };

    private void register(DemoLoyaltyServiceImpl serviceImpl, String openid, String appId) {

        try {
            serviceImpl.syncFollowerToLoyalty(openid, appId);
            serviceImpl.subscribePublicNoUpate(openid, appId);
            serviceImpl.recordSubscribe(openid, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleImageMessage(EventMessage message) throws Exception {
    };

    public void handleVoiceMessage(EventMessage message) throws Exception {
    };

    public void handleVideoMessage(EventMessage message) throws Exception {
    };

    public void handleShortVideoMessage(EventMessage message) throws Exception {
    };

    public void handleMsgLocationMessage(EventMessage message) throws Exception {
    };

    public void handleEventLocationEvent(EventMessage event) throws Exception {
    };

}
