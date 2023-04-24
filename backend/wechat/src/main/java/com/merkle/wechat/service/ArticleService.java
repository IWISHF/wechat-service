package com.merkle.wechat.service;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.Article;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.Pagination;

public interface ArticleService {

    void createArticle(Article article, WechatPublicNo pbNo) throws Exception;

    void udpateArticle(Article article, Long wechatPublicNoId, Long articleId) throws Exception;

    void delete(Long articleId, Long wechatPublicNoId) throws Exception;

    Pagination<Article> search(Long wechatPublicNoId, String key, Pageable pageable) throws Exception;

    Article findByArticleId(long replyArticleId);

    Article findArticle(Long articleId, Long channelId);

}
