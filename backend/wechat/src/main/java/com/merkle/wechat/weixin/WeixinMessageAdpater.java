package com.merkle.wechat.weixin;

import com.merkle.wechat.common.entity.AutoReplyRule;

public interface WeixinMessageAdpater {

    void sendTextMessage(String toUser, String content, String appId);

    void sendArticle(String toUser, AutoReplyRule rule, String appId, Long wechatPublicNoId);

    void sendPicture(String toUser, AutoReplyRule rule, String appId);

    void sendMPNews(String toUser, AutoReplyRule rule, String appId);

    void sendVideo(String toUser, AutoReplyRule rule, String appId);

}
