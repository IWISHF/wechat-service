package com.merkle.wechat.service;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.ArticleDao;
import com.merkle.wechat.common.dao.AutoReplyRuleDao;
import com.merkle.wechat.common.entity.Article;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.Pagination;

@Component
public class ArticleServiceImpl implements ArticleService {
    private @Autowired ArticleDao articleDao;
    private @Autowired AutoReplyRuleDao autoReplyRuleDao;

    @Override
    public void createArticle(Article article, WechatPublicNo pbNo) throws Exception {
        article.setWechatPublicNoId(pbNo.getId());
        articleDao.save(article);
    }

    @Override
    public void udpateArticle(Article article, Long wechatPublicNoId, Long articleId) throws Exception {
        Article dbArticle = Optional.ofNullable(articleDao.findByWechatPublicNoIdAndId(wechatPublicNoId, articleId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        BeanUtils.copyProperties(dbArticle, article);
        dbArticle.setId(articleId);
        dbArticle.setUpdated(new Date());
        articleDao.save(article);
    }

    @Override
    public void delete(Long articleId, Long wechatPublicNoId) throws Exception {
        if (autoReplyRuleDao.existsByReplyArticleId(articleId)) {
            throw new ServiceWarn(ExceptionConstants.ARTICLE_CANT_DELETE);
        }
        articleDao.deleteByIdAndWechatPublicNoId(articleId, wechatPublicNoId);
    }

    @Override
    public Pagination<Article> search(Long wechatPublicNoId, String key, Pageable pageable) throws Exception {
        Page<Article> page = articleDao.findByTitleContainingAndWechatPublicNoId(key, wechatPublicNoId, pageable);
        return new Pagination<>(page);
    }

    @Override
    public Article findByArticleId(long replyArticleId) {
        return articleDao.findOne(replyArticleId);
    }

    @Override
    public Article findArticle(Long articleId, Long channelId) {
        return articleDao.findByWechatPublicNoIdAndId(channelId, articleId);
    }

}
