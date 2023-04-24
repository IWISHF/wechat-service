package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.DefaultAutoReplyDao;
import com.merkle.wechat.common.dao.KeywordsAutoReplyDao;
import com.merkle.wechat.common.dao.QrcodeDao;
import com.merkle.wechat.common.dao.menu.MenuClickActionDao;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.DefaultAutoReply;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.KeywordsAutoReply;
import com.merkle.wechat.common.entity.Qrcode;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.TriggerTextKey;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.menu.MenuClickAction;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.statistics.QrcodeStatisticsService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.autoreply.KeywordsAutoReplyRuleVo;
import com.merkle.wechat.vo.autoreply.converter.AutoReplyRuleConverter;
import com.merkle.wechat.weixin.WeixinMessageAdpater;

@Component
public class AutoReplyServiceImpl implements AutoReplyService {

    private @Autowired QrcodeDao qrcodeDaoImpl;
    private @Autowired MenuClickActionDao menuClickActionDaoImpl;
    private @Autowired DefaultAutoReplyDao defaultAutoReplyDaoImpl;
    private @Autowired WeixinMessageAdpater weixinMessageAdapterImpl;
    private @Autowired AutoReplyRuleConverter autoReplyRuleConverter;
    private @Autowired KeywordsAutoReplyDao keywordsAutoReplyRulesDaoImpl;
    private @Autowired FollowerService followerServiceImpl;
    private @Autowired QrcodeStatisticsService qrcodeStatisticsServiceImpl;

    @Override
    public Pagination<KeywordsAutoReply> search(Long channelId, String key, Pageable pageable) {
        Page<KeywordsAutoReply> page = keywordsAutoReplyRulesDaoImpl.findByNameContainingAndWechatPublicNoId(key,
                channelId, pageable);
        return new Pagination<>(page);
    }

    @Override
    public void updateKeywordsAutoReply(KeywordsAutoReply autoReply, Long keywordsAutoReplyId, Long channelId)
            throws Exception {
        KeywordsAutoReply dbAutoReply = Optional
                .ofNullable(
                        keywordsAutoReplyRulesDaoImpl.findOneByIdAndWechatPublicNoId(keywordsAutoReplyId, channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        dbAutoReply.setName(autoReply.getName());
        dbAutoReply.setEnable(autoReply.isEnable());
        dbAutoReply.getAutoReplyrules().clear();
        dbAutoReply.getAutoReplyrules().addAll(autoReply.getAutoReplyrules());
        dbAutoReply.getTriggerKeys().clear();
        dbAutoReply.getTriggerKeys().addAll(autoReply.getTriggerKeys());
        dbAutoReply.setId(keywordsAutoReplyId);
        dbAutoReply.setUpdatedDate(new Date());
        keywordsAutoReplyRulesDaoImpl.save(dbAutoReply);
    }

    @Override
    public void delete(Long keywordsAutoReplyId, Long channelId) throws Exception {
        KeywordsAutoReply dbAutoReply = Optional
                .ofNullable(
                        keywordsAutoReplyRulesDaoImpl.findOneByIdAndWechatPublicNoId(keywordsAutoReplyId, channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        keywordsAutoReplyRulesDaoImpl.delete(dbAutoReply);
    }

    @Override
    public KeywordsAutoReplyRuleVo getKeywordsAutoReplyDetail(Long keywordsId, Long channelId) {
        KeywordsAutoReply autoReply = Optional
                .ofNullable(keywordsAutoReplyRulesDaoImpl.findByIdAndWechatPublicNoId(keywordsId, channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        KeywordsAutoReplyRuleVo vo = new KeywordsAutoReplyRuleVo();
        BeanUtils.copyProperties(autoReply, vo, "autoReplyrules");
        vo.setAutoReplyrules(autoReplyRuleConverter.convertAutoReplyRules(autoReply.getAutoReplyrules(), channelId));
        return vo;
    }

    @Override
    public void triggerKeywordsAutoReply(Long keywordAutoReplyId, Long channelId, boolean enable) throws Exception {
        KeywordsAutoReply dbAutoReply = Optional
                .ofNullable(keywordsAutoReplyRulesDaoImpl.findOneByIdAndWechatPublicNoId(keywordAutoReplyId, channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        dbAutoReply.setEnable(enable);
        dbAutoReply.setUpdatedDate(new Date());
        keywordsAutoReplyRulesDaoImpl.save(dbAutoReply);

    }

    @Override
    public void createKeywordsAutoReply(KeywordsAutoReply autoReply, WechatPublicNo pbNo) throws Exception {
        autoReply.setToUserName(pbNo.getUserName());
        autoReply.setWechatPublicNoId(pbNo.getId());
        keywordsAutoReplyRulesDaoImpl.save(autoReply);
    }

    @Override
    public List<KeywordsAutoReply> findActiveRulesByToUserName(String toUserName) {
        return keywordsAutoReplyRulesDaoImpl.findByToUserNameAndEnable(toUserName, true);
    }

    @Override
    public void replyClickEvent(EventMessage event) {
        List<MenuClickAction> actions = findActiveClickActionByToUserName(event.getToUserName());
        if (actions == null || actions.size() == 0) {
            return;
        }
        for (MenuClickAction action : actions) {
            if (action.getKeycode().equals(event.getEventKey())) {
                AutoReplyRule rule = action.getRule();
                if (rule != null) {
                    replyMessage(rule, event, action.getWechatPublicNoId());
                }
                break;
            }
        }
    }

    private List<MenuClickAction> findActiveClickActionByToUserName(String toUserName) {
        return menuClickActionDaoImpl.findByToUserNameAndEnable(toUserName, true);
    }

    @Override
    public void replyTextMessage(EventMessage message) {
        List<KeywordsAutoReply> keywordsAutoReplys = findActiveRulesByToUserName(message.getToUserName());
        if (keywordsAutoReplys == null || keywordsAutoReplys.size() == 0) {
            replyDefaultUnHitKeywordsMessage(message);
            return;
        }
        boolean matchAny = false;

        for (KeywordsAutoReply keywordsAutoReply : keywordsAutoReplys) {
            Set<TriggerTextKey> triggerKeys = keywordsAutoReply.getTriggerKeys();
            String text = message.getContent();
            boolean match = false;
            for (TriggerTextKey triggerKey : triggerKeys) {
                if (triggerKey.isCompleteMatch()) {
                    match = triggerKey.getKey().equalsIgnoreCase(text);
                } else {
                    match = text.contains(triggerKey.getKey()) || triggerKey.getKey().contains(text);
                }
                if (match) {
                    break;
                }
            }
            if (match) {
                Set<AutoReplyRule> autoReplyrules = keywordsAutoReply.getAutoReplyrules();
                if (autoReplyrules != null && autoReplyrules.size() > 0) {
                    replyMessage(autoReplyrules, message, keywordsAutoReply.getWechatPublicNoId());
                }
            }
            matchAny = matchAny || match;
        }

        if (!matchAny) {
            replyDefaultUnHitKeywordsMessage(message);
        }

    }

    @Override
    public void replyScanEvent(EventMessage event) {
        List<Qrcode> qrcodes = findActiveQrcodeRules(event.getToUserName());
        if (qrcodes == null || qrcodes.size() == 0) {
            return;
        }

        for (Qrcode qr : qrcodes) {
            String sceneContent = qr.getSceneContent();
            if (event.getEventKey() != null && event.getEventKey().contains(sceneContent)) {
                replyScanEvent(event, qr);
                recordScanEventForStatistics(event, qr, false);
                tagMultiTimesSubscribeFollower(event.getFromUserName(), qr);
                break;
            }
        }

    }

    @Override
    public void relpySubscribeEvent(EventMessage event, boolean isNewFollower) {
        if (isNewFollower) {
            replyFirstTimeSubscribeEvent(event);
        } else {
            replyMultiTimesSubscribeEvent(event);
        }
    }

    @Override
    public void replySubscribeScanEvent(EventMessage event, boolean isNewFollower) {
        if (isNewFollower) {
            replyFirstTimeSubscribeScanEvent(event);
        } else {
            replyMultiTimesSubscribeScanEvent(event);
        }

    }

    private void recordScanEventForStatistics(EventMessage event, Qrcode qr, boolean isSubscribe) {
        try {
            AsyncUtil.asyncRun(() -> {
                qrcodeStatisticsServiceImpl.create(event, qr, isSubscribe);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replyFirstTimeSubscribeScanEvent(EventMessage event) {
        List<Qrcode> qrcodes = findActiveQrcodeRules(event.getToUserName());
        if (qrcodes == null || qrcodes.size() == 0) {
            return;
        }

        for (Qrcode qr : qrcodes) {
            String sceneContent = qr.getSceneContent();
            if (event.getEventKey() != null && event.getEventKey().contains(sceneContent)) {

                if (!qr.isCloseSubscribeAutoReply()) {
                    replyFirstTimeSubscribeEvent(event);
                }

                replyScanEvent(event, qr);

                recordScanEventForStatistics(event, qr, true);

                tagFirstTimeSubscribeFollower(event.getFromUserName(), qr);

                break;
            }
        }

    }

    private void replyMultiTimesSubscribeScanEvent(EventMessage event) {
        List<Qrcode> qrcodes = findActiveQrcodeRules(event.getToUserName());
        if (qrcodes == null || qrcodes.size() == 0) {
            return;
        }

        for (Qrcode qr : qrcodes) {
            String sceneContent = qr.getSceneContent();
            if (event.getEventKey() != null && event.getEventKey().contains(sceneContent)) {

                if (!qr.isCloseSubscribeAutoReply()) {
                    replyMultiTimesSubscribeEvent(event);
                }

                replyScanEvent(event, qr);

                recordScanEventForStatistics(event, qr, true);

                tagMultiTimesSubscribeFollower(event.getFromUserName(), qr);

                break;
            }
        }
    }

    private void replyScanEvent(EventMessage event, Qrcode qr) {
        Set<AutoReplyRule> autoReplyrules = qr.getAutoReplyrules();
        if (autoReplyrules != null && autoReplyrules.size() > 0) {
            replyMessage(autoReplyrules, event, qr.getWechatPublicNoId());
        }
    }

    private void tagFirstTimeSubscribeFollower(String fromUserName, Qrcode qr) {
        if (!qr.isAutoTagNewSubscribeUser() || qr.getNewSubscribeTags().size() == 0) {
            return;
        }
        tagFollower(fromUserName, qr.getNewSubscribeTags(), qr.getAppId());
    }

    private void tagMultiTimesSubscribeFollower(String fromUserName, Qrcode qr) {
        if (!qr.isAutoTagAlreadySubscribeUser() || qr.getAlreadySubscribeTags().size() == 0) {
            return;
        }
        tagFollower(fromUserName, qr.getAlreadySubscribeTags(), qr.getAppId());
    }

    private void tagFollower(String fromUserName, Set<Tag> tags, String pbNoAppId) {
        followerServiceImpl.tagFollower(fromUserName, tags, pbNoAppId);
    }

    private void replyDefaultUnHitKeywordsMessage(EventMessage message) {
        DefaultAutoReply unHitKeywordsAutoReply = findActiveUnHitRule(message.getToUserName());
        replyByDefaultAuoReplyRule(message, unHitKeywordsAutoReply);
    }

    private List<Qrcode> findActiveQrcodeRules(String toUserName) {
        return qrcodeDaoImpl.findByToUserNameAndEnable(toUserName, true);
    }

    private void replyFirstTimeSubscribeEvent(EventMessage event) {
        DefaultAutoReply rule = findActiveFirstSubscribeDefaultAutoReplyRule(event.getToUserName());
        replyByDefaultAuoReplyRule(event, rule);
    }

    private void replyMultiTimesSubscribeEvent(EventMessage event) {
        DefaultAutoReply rule = findActiveMultiSubscribeDefaultAutoReplyRule(event.getToUserName());
        replyByDefaultAuoReplyRule(event, rule);
    }

    private void replyByDefaultAuoReplyRule(EventMessage message, DefaultAutoReply rule) {
        if (rule == null) {
            return;
        }
        Set<AutoReplyRule> autoReplyrules = rule.getAutoReplyrules();
        if (autoReplyrules != null && autoReplyrules.size() > 0) {
            replyMessage(autoReplyrules, message, rule.getWechatPublicNoId());
        }
    }

    private DefaultAutoReply findActiveFirstSubscribeDefaultAutoReplyRule(String toUserName) {
        return defaultAutoReplyDaoImpl.findByToUserNameAndEnableAndType(toUserName, true,
                DefaultAutoReply.FIRST_SUBSCRIBE_REPLY);
    }

    private DefaultAutoReply findActiveMultiSubscribeDefaultAutoReplyRule(String toUserName) {
        return defaultAutoReplyDaoImpl.findByToUserNameAndEnableAndType(toUserName, true,
                DefaultAutoReply.MULTI_SUBSCRIBE_REPLY);
    }

    private DefaultAutoReply findActiveUnHitRule(String toUserName) {
        return defaultAutoReplyDaoImpl.findByToUserNameAndEnableAndType(toUserName, true,
                DefaultAutoReply.UN_HIT_KEYWORDS_REPLY);
    }

    private void replyMessage(Set<AutoReplyRule> rules, EventMessage message, Long wechatPublicNoId) {
        rules.forEach((rule) -> {
            replyMessage(rule, message, wechatPublicNoId);
        });
    }

    private void replyMessage(AutoReplyRule rule, EventMessage message, Long wechatPublicNoId) {
        switch (rule.getReplyType()) {
            case AutoReplyRule.REPLY_TEXT: {
                weixinMessageAdapterImpl.sendTextMessage(message.getFromUserName(), rule.getReplyTexts(),
                        message.getAppId());
                break;
            }
            case AutoReplyRule.REPLY_ARTICLE: {
                weixinMessageAdapterImpl.sendArticle(message.getFromUserName(), rule, message.getAppId(),
                        wechatPublicNoId);
                break;
            }
            case AutoReplyRule.REPLY_PICTURE: {
                weixinMessageAdapterImpl.sendPicture(message.getFromUserName(), rule, message.getAppId());
                break;
            }
            case AutoReplyRule.REPLY_VIDEO: {
                weixinMessageAdapterImpl.sendVideo(message.getFromUserName(), rule, message.getAppId());
                break;
            }
            case AutoReplyRule.REPLY_MPNEWS: {
                weixinMessageAdapterImpl.sendMPNews(message.getFromUserName(), rule, message.getAppId());
                break;
            }
        }

    }

}
