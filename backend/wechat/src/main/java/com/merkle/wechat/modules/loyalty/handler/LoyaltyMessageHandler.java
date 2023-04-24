package com.merkle.wechat.modules.loyalty.handler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.loyalty.LoyaltyConfig;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.consumer.MessageConsumer;
import com.merkle.wechat.handler.MessageHandler;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.service.loyalty.LoyaltyConfigService;

@DependsOn(value = "MessageConsumer")
@Component(value = "LoyaltyPlatformHandler")
public class LoyaltyMessageHandler implements MessageHandler {
    protected Logger logger = LoggerFactory.getLogger("LoyaltyPlatformHandler");
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired LoyaltyConfigService loyaltyConfigServiceImpl;

    @Autowired(required = true)
    @Qualifier("MessageConsumer")
    private MessageConsumer messageConsumer;

    @PostConstruct
    public void register() throws Exception {
        logger.info("=========== register loyalty message handler =========");
        messageConsumer.registerHandler(this);
    }

    public boolean isSupport(EventMessage message) throws Exception {
        String appId = message.getAppId();
        LoyaltyConfig loyaltyConfig = loyaltyConfigServiceImpl.getLoyaltyCosnfigByAppId(appId);
        if (loyaltyConfig == null) {
            return false;
        }
        return true;
    }

    public void handleTextMessage(EventMessage message) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordTextEvent(message);
        });
    }

    public void handleLinkMessage(EventMessage message) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordLinkEvent(message);
        });
    }

    public void handleSubscribeEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            try {
                loyaltyServiceImpl.syncFollowerToLoyalty(event.getFromUserName(), event.getAppId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleUnSubscribeEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordUnsubscribeEvent(event);
        });
    };

    public void handleClickEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordMenuClickEvent(event);
        });
    }

    public void handleScanEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordScanEvent(event);
        });
    }

    public void handleScanCodePushEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordWXEvent("wechat-scan-code-push", event);
        });
    }

    public void handleScanCodeWaitingMsgEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordWXEvent("wechat-scan-code-waiting-msg", event);
        });
    }

    public void handleViewEvent(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            loyaltyServiceImpl.recordMenuViewEvent(event);
        });
    }

}
