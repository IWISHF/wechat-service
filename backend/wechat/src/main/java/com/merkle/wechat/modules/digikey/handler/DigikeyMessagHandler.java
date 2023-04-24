package com.merkle.wechat.modules.digikey.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.consumer.MessageConsumer;
import com.merkle.wechat.handler.MessageHandler;
import com.merkle.wechat.modules.digikey.service.DigikeyService;
import com.merkle.wechat.service.ThirdPartyService;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.follower.FollowerBindInfoService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.NewsMessage.Article;

@Component(value = "DigikeyMessageHandler")
@DependsOn(value = "MessageConsumer")
@ConditionalOnProperty(prefix = "merkle.digikey.demo", name = "enable")
public class DigikeyMessagHandler implements MessageHandler {
    protected Logger logger = LoggerFactory.getLogger("DigikeyMessageHandler");

    @Autowired(required = true)
    @Qualifier("MessageConsumer")
    private MessageConsumer messageConsumer;
    private @Autowired ThirdPartyService ttpServiceImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired DigikeyService digikeyServiceImpl;
    private @Autowired FollowerBindInfoService followerBindInfoServiceImpl;
    private @Autowired WechatPublicNoService wechatPublicNoServiceImpl;

    @PostConstruct
    public void register() throws Exception {
        logger.info("=========== register digikey message handler =========");
        messageConsumer.registerHandler(this);
    }

    public void handleTextMessage(EventMessage message) throws Exception {
        if (message.getContent().equals("来福") && message.getToUserName().equalsIgnoreCase("gh_f97b5623abba")) {
            Message picMessage = new NewsMessage(message.getFromUserName(), generateArticleLst());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(message.getAppId()),
                    picMessage);
        }
    }

    public void handleSubscribeEvent(EventMessage event) throws Exception {
        if (!event.getToUserName().equalsIgnoreCase("gh_f97b5623abba")) {
            return;
        }
        FollowerBindInfo bindInfo = followerBindInfoServiceImpl.findOneByOpenid(event.getFromUserName());
        if (bindInfo != null) {
            digikeyServiceImpl.tagVipForDigikey(bindInfo.getOpenid(),
                    wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(bindInfo.getWechatPublicNoId()));
        }
        if (event.getEventKey() != null && event.getEventKey().contains("cny2019")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleLst());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null && event.getEventKey().contains("ElectronicaCampaignPage")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleLstForMuniheiCampaign());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null && event.getEventKey().contains("180620-Feature02-wk84_工程师小贴士_SMT_V1")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign22());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null && event.getEventKey().contains("190109-阻值太低,无法测量来试试开尔文测试")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign26());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null
                && event.getEventKey().contains("180919-Feature02-wk96_工程师小贴士_RS-485_V1")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign27());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null
                && event.getEventKey().contains("181010-Feature02-wk98_工程师小贴士_connector_V1")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign30());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        }
    }

    public void handleScanEvent(EventMessage event) throws Exception {
        if (!event.getToUserName().equalsIgnoreCase("gh_f97b5623abba")) {
            return;
        }
        if (event.getEventKey() != null && event.getEventKey().equals("cny2019")) {
            // Message message = new TextMessage(event.getFromUserName(),
            // "得捷电子金猪旺礼活动，戳：<a href='" + ttpServiceImpl.getBackendDomain() +
            // "/wechat/7/oauth?redirect="
            // + ttpServiceImpl.getFrontDomain() + "/h5/djk/campaign'>这里</a>");
            // MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()),
            // message);
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleLst());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null && event.getEventKey().contains("ElectronicaCampaignPage")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleLstForMuniheiCampaign());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null && event.getEventKey().contains("180620-Feature02-wk84_工程师小贴士_SMT_V1")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign22());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null && event.getEventKey().contains("190109-阻值太低,无法测量来试试开尔文测试")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign26());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null
                && event.getEventKey().contains("180919-Feature02-wk96_工程师小贴士_RS-485_V1")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign27());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        } else if (event.getEventKey() != null
                && event.getEventKey().contains("181010-Feature02-wk98_工程师小贴士_connector_V1")) {
            Message picMessage = new NewsMessage(event.getFromUserName(), generateArticleListForMuniheiCampaign30());
            MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(event.getAppId()), picMessage);
        }
    }

    private List<Article> generateArticleLst() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("得捷电子金猪旺礼活动");
        article.setDescription("参与活动赢奖品啦！");
        article.setPicurl("https://cdn-loyalty-dev.merklechina.com/djk/images/djk-cny.jpg");
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/7/oauth?redirect=" + ttpServiceImpl.getFrontDomain()
                + "/h5/djk/campaign");
        articleList.add(article);
        return articleList;
    }

    private List<Article> generateArticleLstForMuniheiCampaign() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("登记有礼！");
        article.setDescription("慕尼黑上海电子展");
        article.setPicurl("https://cdn-lp-cn1-wechat-production.merklechina.com/djk/images/djk-logo.png");
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/7/oauth?redirect=" + ttpServiceImpl.getFrontDomain()
                + "/h5/digikey/electronica?campaign=1");
        articleList.add(article);
        return articleList;
    }

    private List<Article> generateArticleListForMuniheiCampaign22() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("SMT焊盘氧化可以避免吗？");
        article.setDescription("慕尼黑上海电子展");
        article.setPicurl("https://cdn-lp-cn1-wechat-production.merklechina.com/djk/images/djk-logo.png");
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/7/oauth?redirect="
                + "https://lp-cn1-wechat-production.merklechina.com/post/digikey/22");
        articleList.add(article);
        return articleList;
    }

    private List<Article> generateArticleListForMuniheiCampaign26() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("阻值太低，无法测量？来试试开开尔文测试吧!");
        article.setDescription("慕尼黑上海电子展");
        article.setPicurl("https://cdn-lp-cn1-wechat-production.merklechina.com/djk/images/djk-logo.png");
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/7/oauth?redirect="
                + "https://lp-cn1-wechat-production.merklechina.com/post/digikey/26");
        articleList.add(article);
        return articleList;
    }

    private List<Article> generateArticleListForMuniheiCampaign27() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("涨知识 | 长距有线通信，为什么要用RS-485？");
        article.setDescription("慕尼黑上海电子展");
        article.setPicurl("https://cdn-lp-cn1-wechat-production.merklechina.com/djk/images/djk-logo.png");
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/7/oauth?redirect="
                + "https://lp-cn1-wechat-production.merklechina.com/post/digikey/27");
        articleList.add(article);
        return articleList;
    }

    private List<Article> generateArticleListForMuniheiCampaign30() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("连接器能承载多大电流，哪些因素说了算？");
        article.setDescription("慕尼黑上海电子展");
        article.setPicurl("https://cdn-lp-cn1-wechat-production.merklechina.com/djk/images/djk-logo.png");
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/7/oauth?redirect="
                + "https://lp-cn1-wechat-production.merklechina.com/post/digikey/30");
        articleList.add(article);
        return articleList;
    }
}
