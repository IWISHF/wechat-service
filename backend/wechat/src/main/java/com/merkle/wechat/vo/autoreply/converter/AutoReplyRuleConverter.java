package com.merkle.wechat.vo.autoreply.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.ArticleDao;
import com.merkle.wechat.common.dao.MaterialAssetDao;
import com.merkle.wechat.common.dao.mpnews.MpnewsDao;
import com.merkle.wechat.common.entity.Article;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.mpnews.Mpnews;
import com.merkle.wechat.vo.autoreply.AutoReplyRuleVo;

@Component
public class AutoReplyRuleConverter {

    private @Autowired ArticleDao articleDaoImpl;
    private @Autowired MpnewsDao mpnewsDaoImpl;
    private @Autowired MaterialAssetDao materialAssetDaoImpl;

    @SuppressWarnings("rawtypes")
    public List<AutoReplyRuleVo> convertAutoReplyRules(Set<AutoReplyRule> autoReplyrules, Long pbNoId) {
        List<AutoReplyRuleVo> vos = new ArrayList<>();
        autoReplyrules.forEach((rule) -> {
            vos.add(convertAutoReplyRule(rule, pbNoId));
        });
        return vos;
    }

    @SuppressWarnings("rawtypes")
    public AutoReplyRuleVo convertAutoReplyRule(AutoReplyRule rule, Long pbNoId) {
        switch (rule.getReplyType()) {
            case AutoReplyRule.REPLY_ARTICLE: {
                return convertToArticle(rule, pbNoId);
            }
            case AutoReplyRule.REPLY_MPNEWS: {

                return convertToMpnews(rule, pbNoId);
            }
            case AutoReplyRule.REPLY_PICTURE:
            case AutoReplyRule.REPLY_VIDEO: {
                return convertToMaterial(rule, pbNoId);
            }
            default: {
                return copyToAutoReplyRuleVo(rule);
            }
        }
    }

    public AutoReplyRuleVo<Article> convertToArticle(AutoReplyRule rule, Long wechatPublicNoId) {
        AutoReplyRuleVo<Article> vo = new AutoReplyRuleVo<>();
        Article article = articleDaoImpl.findOne(rule.getReplyArticleId());
        BeanUtils.copyProperties(rule, vo);
        vo.setItem(article);
        return vo;
    }

    public AutoReplyRuleVo<Mpnews> convertToMpnews(AutoReplyRule rule, Long wechatPublicNoId) {
        AutoReplyRuleVo<Mpnews> vo = new AutoReplyRuleVo<>();
        Mpnews news = mpnewsDaoImpl.findByWechatPublicNoIdAndMediaId(wechatPublicNoId, rule.getMediaId());
        BeanUtils.copyProperties(rule, vo);
        vo.setItem(news);
        return vo;
    }

    public AutoReplyRuleVo<MaterialAsset> convertToMaterial(AutoReplyRule rule, Long wechatPublicNoId) {
        AutoReplyRuleVo<MaterialAsset> vo = new AutoReplyRuleVo<>();
        MaterialAsset asset = materialAssetDaoImpl.findByWechatPublicNoIdAndMediaId(wechatPublicNoId,
                rule.getMediaId());
        BeanUtils.copyProperties(rule, vo);
        vo.setItem(asset);
        return vo;
    }

    public AutoReplyRuleVo<AutoReplyRule> copyToAutoReplyRuleVo(AutoReplyRule rule) {
        AutoReplyRuleVo<AutoReplyRule> vo = new AutoReplyRuleVo<>();
        BeanUtils.copyProperties(rule, vo);
        return vo;
    }
}
