package com.merkle.wechat.modules.merkle.annual.handler;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.merkle.wechat.common.dao.AnnualVoteDao;
import com.merkle.wechat.common.entity.AnnualVote;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.consumer.MessageConsumer;
import com.merkle.wechat.handler.MessageHandler;
import com.merkle.wechat.modules.demo.service.DemoTagService;
import com.merkle.wechat.service.ThirdPartyService;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.follower.FollowerService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

@Component(value = "AnnualPartyMessageHandler")
@DependsOn(value = "MessageConsumer")
@ConditionalOnProperty(prefix = "merkle.annual.party", name = "enable")
public class AnnualPartyMessageHandler implements MessageHandler {
    protected Logger logger = LoggerFactory.getLogger("AnnualPartyMessageHandler");

    @Autowired(required = true)
    @Qualifier("MessageConsumer")
    private MessageConsumer messageConsumer;

    private @Autowired DemoTagService tagServiceImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired FollowerService followerServiceImpl;
    private @Autowired ThirdPartyService ttpServiceImpl;

    @PostConstruct
    public void register() throws Exception {
        logger.info("=========== register annual party message handler =========");
        messageConsumer.registerHandler(this);
    }

    @Override
    public void handleSubscribeEvent(EventMessage event) throws Exception {
        if (event.getEventKey() != null && event.getEventKey().equals("qrscene_annualParty")) {
            // if (isExpire()) {
            // return;
            // }
            AnnualVote vote = annualVoteDao.findOneByOpenid(event.getFromUserName());
            if (vote != null) {
                return;
            }
            AsyncUtil.asyncRun(() -> {
                addAnnualTagAndSendHelloMessage(tagServiceImpl, tokenServiceImpl, event.getFromUserName(),
                        event.getAppId());
                createRecord(followerServiceImpl, event.getFromUserName(), event.getAppId());
            });
        }
    }

    @Override
    public void handleScanEvent(EventMessage event) throws Exception {
        if (event.getEventKey() != null && event.getEventKey().equals("annualParty")) {
            // if (isExpire()) {
            // return;
            // }
            AnnualVote vote = annualVoteDao.findOneByOpenid(event.getFromUserName());
            if (vote != null) {
                return;
            }
            AsyncUtil.asyncRun(() -> {
                addAnnualTagAndSendHelloMessage(tagServiceImpl, tokenServiceImpl, event.getFromUserName(),
                        event.getAppId());
                createRecord(followerServiceImpl, event.getFromUserName(), event.getAppId());
            });
        }
    }

    private void addAnnualTagAndSendHelloMessage(DemoTagService tagServiceImpl, TokenService tokenServiceImpl,
            String openid, String appId) {
        // tagServiceImpl.tagFollower(openid, "annual");
        Message message = new TextMessage(openid, "您已签到成功，<a href='" + ttpServiceImpl.getBackendDomain()
                + "/wechat/3/oauth?redirect=" + ttpServiceImpl.getFrontDomain() + "/annual/index.html'>点我参与投票</a>");
        MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
    }

    private @Autowired AnnualVoteDao annualVoteDao;

    private void createRecord(FollowerService followerServiceImpl, String openid, String appId) {
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            try {
                follower = followerServiceImpl.findOrCreateFollower(openid, appId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AnnualVote vote = annualVoteDao.findOneByOpenid(openid);
        if (vote != null) {
            return;
        }
        vote = new AnnualVote();
        vote.setOpenid(openid);
        vote.setNickname(Base64Utils.encodeToString(follower.getNickname().getBytes()));
        vote.setHeadimgurl(follower.getHeadimgurl());
        annualVoteDao.save(vote);
    }

    @SuppressWarnings("unused")
    private boolean isExpire() {
        Calendar c = Calendar.getInstance();
        c.set(2019, 0, 18, 19, 30);
        return System.currentTimeMillis() > c.getTimeInMillis();
    }
}
