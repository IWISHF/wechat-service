package com.merkle.wechat.service.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.RewardsRedeemLogDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.dao.loyalty.LoyaltyRewardConfigDao;
import com.merkle.wechat.common.dao.template.WeixinTemplateMessageDao;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.loyalty.LoyaltyRewardConfig;
import com.merkle.wechat.common.entity.template.WeixinExpressTemplateConfig;
import com.merkle.wechat.common.entity.template.WeixinNoticeTemplateConfig;
import com.merkle.wechat.common.entity.template.WeixinTemplate;
import com.merkle.wechat.common.entity.template.WeixinTemplateContentItem;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.loyalty.DynamicValueService;
import com.merkle.wechat.vo.Pagination;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.GetAllPrivateTemplateResult;
import weixin.popular.bean.message.PrivateTemplate;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;

@Component
public class WeixinTemplateMessageServiceImpl implements WeixinTemplateMessageService {
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired WeixinTemplateMessageDao templateMessageDaoImpl;
    private @Autowired LoyaltyRewardConfigDao loyaltyRewardConfigDaoImpl;
    private @Autowired DynamicValueService dynamicValueServiceImpl;
    private @Autowired RewardsRedeemLogDao rewardsRedeemLogDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;

    @Override
    public void syncAddedTemplate(Long pbNoId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(pbNoId);
        GetAllPrivateTemplateResult result = MessageAPI.templateGet_all_private_template(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()));
        if (result.isSuccess()) {
            List<PrivateTemplate> templateList = result.getTemplate_list();
            List<WeixinTemplate> needToSave = new ArrayList<>();
            for (PrivateTemplate t : templateList) {
                WeixinTemplate dbTemplate = templateMessageDaoImpl
                        .findOneByTemplateIdAndWechatPublicNoId(t.getTemplate_id(), pbNoId);
                if (dbTemplate != null) {
                    continue;
                }

                WeixinTemplate template = new WeixinTemplate();
                template.setTemplateId(t.getTemplate_id());
                template.setContent(t.getContent());
                template.setDeputyIndustry(t.getDeputy_industry());
                template.setEnable(true);
                template.setExample(t.getExample());
                template.setPrimaryIndustry(t.getPrimary_industry());
                template.setTitle(t.getTitle());
                template.setWechatPublicNoId(pbNoId);
                template.setUpdatedDate(new Date());
                // get all keywords
                Pattern p = Pattern.compile("(?<=\\{)\\w+");
                Matcher m = p.matcher(template.getContent());
                int i = 1;
                Set<WeixinTemplateContentItem> keywords = template.getKeywords();
                while (m.find()) {
                    WeixinTemplateContentItem item = new WeixinTemplateContentItem();
                    item.setField(m.group());
                    item.setOrderIndex(i++);
                    keywords.add(item);
                }
                WeixinTemplateContentItem item = new WeixinTemplateContentItem();
                item.setField("url");
                item.setOrderIndex(i++);
                keywords.add(item);
                needToSave.add(template);
            }
            templateMessageDaoImpl.save(needToSave);
        } else {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
    }

    @Override
    public Pagination<WeixinTemplate> search(Long pbNoId, String key, Pageable pageable) {
        Page<WeixinTemplate> page = templateMessageDaoImpl.findByWechatPublicNoIdAndTitleContaining(pbNoId, key,
                pageable);
        return new Pagination<>(page);
    }

    @Override
    public List<WeixinTemplate> getAllActive(Long pbNoId, String key) {
        return templateMessageDaoImpl.findByWechatPublicNoIdAndEnableAndTitleContaining(pbNoId, true, key);
    }

    @Override
    public RewardsRedeemLog sendNoticeTemplateMessage(Follower follower, RewardsRedeemLog redeemLog) {
        LoyaltyRewardConfig rewardConfig = loyaltyRewardConfigDaoImpl.findByAppIdAndRewardId(follower.getPubNoAppId(),
                Integer.valueOf(redeemLog.getRewardId()));
        if (rewardConfig == null || !rewardConfig.isEnable()) {
            redeemLog.setTemplateMessageErrorMessage("not config or disable!");
            return redeemLog;
        }
        TemplateMessage message = new TemplateMessage();
        message.setTouser(follower.getOpenid());
        message.setTemplate_id(rewardConfig.getNoticeTemplateId());
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        for (WeixinNoticeTemplateConfig wc : rewardConfig.getNoticeTemplateConfigs()) {
            if (wc.getField().equals("url") && !StringUtils.isEmpty(wc.getFieldValue())) {
                message.setUrl(wc.getFieldValue());
            } else {
                String value = dynamicValueServiceImpl.getValue(wc, follower, redeemLog);
                generateTemplateMessageItem(value, "#173177", data, wc.getField());
            }
        }
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId()), message);
        redeemLog.setTemplateMessageSendStatus(result.isSuccess());

        if (!result.isSuccess()) {
            redeemLog.setTemplateMessageErrorMessage(result.getErrcode() + "--" + result.getErrmsg());
        }
        return redeemLog;
    }

    @Override
    public void sendExpressTemplateMessage(String trackingCode, Long logid, Long channelId) throws Exception {
        RewardsRedeemLog redeemLog = Optional.ofNullable(rewardsRedeemLogDaoImpl.findOne(logid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        Follower follower = followerDaoImpl.findByOpenidAndPubNoAppId(redeemLog.getOpenid(), redeemLog.getPubNoAppId());
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }
        redeemLog.setTrackingCode(trackingCode);

        LoyaltyRewardConfig rewardConfig = loyaltyRewardConfigDaoImpl.findByAppIdAndRewardId(redeemLog.getPubNoAppId(),
                Integer.valueOf(redeemLog.getRewardId()));
        if (rewardConfig == null || !rewardConfig.isEnable()) {
            redeemLog.setExpressTemplateMessageErrorMessage("not config or disable!");
            rewardsRedeemLogDaoImpl.save(redeemLog);
            return;
        }
        redeemLog.setExpressCompany(rewardConfig.getExpressCompany());
        TemplateMessage message = new TemplateMessage();
        message.setTouser(redeemLog.getOpenid());
        message.setTemplate_id(rewardConfig.getExpressTemplateId());
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        for (WeixinExpressTemplateConfig wc : rewardConfig.getExpressTemplateConfigs()) {
            if (wc.getField().equals("url") && !StringUtils.isEmpty(wc.getFieldValue())) {
                message.setUrl(wc.getFieldValue());
            } else {
                String value = dynamicValueServiceImpl.getValueForExpress(wc, follower, redeemLog);
                generateTemplateMessageItem(value, "#173177", data, wc.getField());
            }
        }
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId()), message);
        redeemLog.setExpressTemplateMessageSendStatus(result.isSuccess());
        redeemLog.setUpdatedDate(new Date());
        redeemLog.setExpressSendedDate(new Date());
        
        if (!result.isSuccess()) {
            redeemLog.setExpressTemplateMessageErrorMessage(result.getErrcode() + "--" + result.getErrmsg());
        }

        rewardsRedeemLogDaoImpl.save(redeemLog);
    }

    private void generateTemplateMessageItem(String value, String color,
            LinkedHashMap<String, TemplateMessageItem> data, String key) {
        TemplateMessageItem item = new TemplateMessageItem();
        item.setValue(value);
        item.setColor(color);
        data.put(key, item);
    }

}
