package com.merkle.wechat.weixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.ArticleService;
import com.merkle.wechat.service.ThirdPartyService;
import com.merkle.wechat.service.TokenService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.ImageMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.MpnewsMessage;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.NewsMessage.Article;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.message.message.VideoMessage;
import weixin.popular.bean.message.message.VideoMessage.Video;

@Component
public class WeixinMessageAdapterImpl implements WeixinMessageAdpater {

    private @Autowired TokenService tokenServiceImpl;
    private @Autowired ThirdPartyService ttpServiceImpl;
    private @Autowired ArticleService articleServiceImpl;

    @Override
    public void sendTextMessage(String toUser, String content, String appId) {
        Message textMessage = new TextMessage(toUser, content);
        sendMessage(textMessage, appId);
    }

    @Override
    public void sendArticle(String toUser, AutoReplyRule rule, String appId, Long wechatPublicNoId) {
        Message articleMessage = new NewsMessage(toUser, generateArticleLst(rule, wechatPublicNoId));
        sendMessage(articleMessage, appId);
    }

    @Override
    public void sendPicture(String toUser, AutoReplyRule rule, String appId) {
        Message picMessage = new ImageMessage(toUser, rule.getMediaId());
        sendMessage(picMessage, appId);
    }

    @Override
    public void sendMPNews(String toUser, AutoReplyRule rule, String appId) {
        Message mpnewsMessage = new MpnewsMessage(toUser, rule.getMediaId());
        sendMessage(mpnewsMessage, appId);
    }

    // TODO:support title and description and thumb_media_id
    @Override
    public void sendVideo(String toUser, AutoReplyRule rule, String appId) {
        Message videoMessage = new VideoMessage(toUser, new Video(rule.getMediaId(), "", ""));
        sendMessage(videoMessage, appId);
    }

    private BaseResult sendMessage(Message message, String appId) {
        return MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
    }

    private List<Article> generateArticleLst(AutoReplyRule rule, Long wechatPublicNoId) {
        List<Article> articleList = new ArrayList<>();
        com.merkle.wechat.common.entity.Article dbArticle = Optional
                .ofNullable(articleServiceImpl.findByArticleId(rule.getReplyArticleId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        Article article = new Article();
        article.setTitle(dbArticle.getTitle());
        article.setDescription(dbArticle.getDescription());
        article.setPicurl(dbArticle.getPicUrl());
        article.setUrl(ttpServiceImpl.getBackendDomain() + "/wechat/" + wechatPublicNoId + "/oauth?redirect="
                + dbArticle.getUrl());
        articleList.add(article);
        return articleList;
    }
}
