package com.merkle.wechat.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.DefaultAutoReplyDao;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.DefaultAutoReply;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.autoreply.DefaultAutoReplyVo;
import com.merkle.wechat.vo.autoreply.converter.AutoReplyRuleConverter;

@Component
public class DefaultAutoReplyServiceImpl implements DefaultAutoReplyService {
    private @Autowired DefaultAutoReplyDao defaultAutoReplyDaoImpl;
    private @Autowired AutoReplyRuleConverter autoReplyConverter;

    @Override
    public void initDefaultRule(WechatPublicNo pbNo) {
        List<DefaultAutoReply> rules = defaultAutoReplyDaoImpl.findByWechatPublicNoId(pbNo.getId());
        if (rules != null && rules.size() > 2) {
            return;
        }
        createDefaultFirstSubscribeReply(pbNo);
        createDefaultMultiSubscribeReply(pbNo);
        createDefaultNotHitKeywordsReply(pbNo);
    }

    private void createDefaultFirstSubscribeReply(WechatPublicNo pbNo) {
        createDefaultReply(pbNo, "首次关注回复", DefaultAutoReply.DEFAULT_FIRST_REPLY_CONTENT,
                DefaultAutoReply.FIRST_SUBSCRIBE_REPLY, 0, DefaultAutoReply.FIRST_SUBSCRIBE_REPLY_FRONT_KEY);
    }

    private void createDefaultMultiSubscribeReply(WechatPublicNo pbNo) {
        createDefaultReply(pbNo, "再次关注回复", DefaultAutoReply.DEFAULT_MULTI_REPLY_CONTENT,
                DefaultAutoReply.MULTI_SUBSCRIBE_REPLY, 1, DefaultAutoReply.MULTI_SUBSCRIBE_REPLY_FRONT_KEY);
    }

    private void createDefaultNotHitKeywordsReply(WechatPublicNo pbNo) {
        createDefaultReply(pbNo, "未命中关键词回复", DefaultAutoReply.DEFAULT_UNHIT_KEYWORDS_REPLY_CONTENT,
                DefaultAutoReply.UN_HIT_KEYWORDS_REPLY, 2, DefaultAutoReply.UN_HIT_KEYWORDS_REPLY_FRONT_KEY);
    }

    private void createDefaultReply(WechatPublicNo pbNo, String name, AutoReplyRule rule, String type, int order,
            String nameKey) {
        DefaultAutoReply autoReply = new DefaultAutoReply();
        autoReply.setEnable(false);
        autoReply.setType(type);
        autoReply.setName(name);
        Set<AutoReplyRule> rules = new HashSet<>();
        rule.setId(null);
        rules.add(rule);
        autoReply.setAutoReplyrules(rules);
        autoReply.setIndexStr(order);
        autoReply.setNameKey(nameKey);
        autoReply.setToUserName(pbNo.getUserName());
        autoReply.setWechatPublicNoId(pbNo.getId());
        defaultAutoReplyDaoImpl.save(autoReply);
    }

    @Override
    public void triggerDefaultAutoReply(Long defaultRuleId, Long channelId, boolean enable) {
        DefaultAutoReply dbAutoReply = Optional
                .ofNullable(defaultAutoReplyDaoImpl.findOneByIdAndWechatPublicNoId(defaultRuleId, channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        dbAutoReply.setEnable(enable);
        dbAutoReply.setUpdatedDate(new Date());
        defaultAutoReplyDaoImpl.save(dbAutoReply);
    }

    @Override
    public void updateDefaultAutoReply(DefaultAutoReply autoReply, Long defaultRuleId, Long channelId)
            throws Exception {
        DefaultAutoReply dbAutoReply = Optional
                .ofNullable(defaultAutoReplyDaoImpl.findOneByIdAndWechatPublicNoId(defaultRuleId, channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        BeanUtils.copyProperties(autoReply, dbAutoReply, "autoReplyrules");
        dbAutoReply.getAutoReplyrules().clear();
        dbAutoReply.getAutoReplyrules().addAll(autoReply.getAutoReplyrules());
        dbAutoReply.setId(defaultRuleId);
        dbAutoReply.setUpdatedDate(new Date());
        defaultAutoReplyDaoImpl.save(dbAutoReply);

    }

    @Override
    public List<DefaultAutoReply> getDefaultAutoReplys(Long channelId) {
        return defaultAutoReplyDaoImpl.findByWechatPublicNoIdOrderByIndexStrAsc(channelId);
    }

    @Override
    public DefaultAutoReplyVo getDefaultAutoReplyDetail(Long defaultAutoReplyId, Long pbNoId) {
        DefaultAutoReply autoReply = Optional
                .ofNullable(defaultAutoReplyDaoImpl.findByWechatPublicNoIdAndId(pbNoId, defaultAutoReplyId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        DefaultAutoReplyVo vo = new DefaultAutoReplyVo();
        BeanUtils.copyProperties(autoReply, vo, "autoReplyrules");
        vo.setAutoReplyrules(autoReplyConverter.convertAutoReplyRules(autoReply.getAutoReplyrules(), pbNoId));
        return vo;
    }

}
