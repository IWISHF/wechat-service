package com.merkle.wechat.modules.digikey.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.digikey.TemplateMessageDataDao;
import com.merkle.wechat.common.dao.digikey.TemplateMessageTaskDao;
import com.merkle.wechat.common.dao.digikey.TemplateMessageTaskItemsDao;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.CampaignOfflineCheckInLog;
import com.merkle.wechat.common.entity.digikey.TemplateMessageData;
import com.merkle.wechat.common.entity.digikey.TemplateMessageTask;
import com.merkle.wechat.common.entity.digikey.TemplateMessageTaskItems;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.GetAllPrivateTemplateResult;
import weixin.popular.bean.message.PrivateTemplate;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;

@Component
public class TemplateMessageServiceImpl {
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired TemplateMessageTaskDao templateMessageTaskDaoImpl;
    private @Autowired TemplateMessageTaskItemsDao templateMessageTaskItemDaoImpl;
    private @Autowired WechatPublicNoService pubNoServiceImpl;
    // DIGIKEY APPID
    private String appId = "wx0a1ea68570a894d5";

    public List<PrivateTemplate> getAllTemplate() {
        GetAllPrivateTemplateResult result = MessageAPI
                .templateGet_all_private_template(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
        if (result.isSuccess()) {
            return result.getTemplate_list();
        } else {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
    }

    public void sendTemplateMessageToMerkleChina(String touser) {
        TemplateMessage message = new TemplateMessage();
        message.setTouser(touser);
        message.setTemplate_id("9gpaXKaLFR19ZWpIbjKyxorApWp4Gl8RQ9NjJM4nDJU");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem("恭喜，您已中奖成为我们全球工程师日抽奖活动的40名获奖者之一！", "#173177", data, "content");
        message.setData(data);
        MessageAPI.messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
    }

    public void sendTemplateMessage(String touser) {
        TemplateMessage message = new TemplateMessage();
        message.setUrl(
                "https://lp-cn1-wechat-production.merklechina.com/h5/digikey/campaign/winner?channel=7&campaign=4&openid="
                        + touser);
        message.setTouser(touser);
        message.setTemplate_id("my5Fkch1ZnONpzVg15iIqtxM_UMFFORfT5KEmOFYIUU");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem("恭喜，您已中奖成为我们全球工程师日抽奖活动的40名获奖者之一！", "#173177", data, "first");
        generateTemplateMessageItem("相当于100美元的Digi-Key购物奖金", "#173177", data, "keyword1");
        generateTemplateMessageItem("2019-04-26", "#173177", data, "keyword2");
        generateTemplateMessageItem("请点击这里填写简单个人资料，购物奖金会直接存进您的Digi-Key网站帐号", "#173177", data, "remark");
        message.setData(data);
        MessageAPI.messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
    }

    private void generateTemplateMessageItem(String value, String color,
            LinkedHashMap<String, TemplateMessageItem> data, String key) {
        TemplateMessageItem item = new TemplateMessageItem();
        item.setValue(value);
        item.setColor(color);
        data.put(key, item);
    }

    private @Autowired TemplateMessageDataDao templateMessageDataDaoImpl;

    public void sendTemplateMessageWithQQVipPreview(String touser, String code) {
        TemplateMessageData data = new TemplateMessageData();
        data.setCode(code);
        data.setOpenid(touser);
        sendTemplateMessageWithQQVip(data);
    }

    public void sendTemplateMessageWithQQVip() {
        Iterable<TemplateMessageData> datas = templateMessageDataDaoImpl.findAll();
        datas.forEach((data) -> {
            sendTemplateMessageWithQQVip(data);
        });
        templateMessageDataDaoImpl.save(datas);
    }

    public void sendTemplateMessageWithQQVip(TemplateMessageData item) {
        TemplateMessage message = new TemplateMessage();
        message.setTouser(item.getOpenid());
        message.setTemplate_id("8tvrYvPxry9Krz0fEVbEuRx2tWHP8gT558WuI6m-olw");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem("恭喜获得慕尼黑上海电子展活动奖品，QQ音乐绿钻豪华版(一个月)!", "#173177", data, "first");
        generateTemplateMessageItem(item.getCode(), "#173177", data, "keyword1");
        generateTemplateMessageItem("2019-04-24", "#173177", data, "keyword2");
        generateTemplateMessageItem(
                "请前往https://y.qq.com/vip/exchange/index.html，登录QQ音乐账号→输入兑换码／验证码→立即兑换，该兑换码须在2019-12-31前进行激活", "#173177",
                data, "remark");
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
        if (result.isSuccess()) {
            item.setSendDate(new Date());
            item.setStatus("send success");
        } else {
            item.setSendDate(new Date());
            item.setStatus(result.getErrcode() + "--" + result.getErrmsg());
        }
    }

    public RewardsRedeemLog sendRedeemRewardsTemplateMessage(Follower follower, RewardsRedeemLog redeemLog) {
        TemplateMessage message = new TemplateMessage();
        message.setTouser(follower.getOpenid());
        message.setTemplate_id("BNAURPj6cdkSihst14Cl0SuwoCV9ciHaCU106mP1LRI");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem("兑换奖品通知", "#173177", data, "first");
        generateTemplateMessageItem(redeemLog.getRewardName(), "#173177", data, "keyword1");
        generateTemplateMessageItem(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "#173177", data,
                "keyword2");
        generateTemplateMessageItem("感谢您的参与，祝生活愉快！\n" + "得捷电子", "#173177", data, "remark");
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId()), message);
        redeemLog.setTemplateMessageSendStatus(result.isSuccess());
        if (!result.isSuccess()) {
            redeemLog.setTemplateMessageErrorMessage(result.getErrcode() + "--" + result.getErrmsg());
        }
        return redeemLog;
    }

    public void commonSendTemplateMessage(Long taskId) {
        TemplateMessageTask task = templateMessageTaskDaoImpl.findOne(taskId);
        List<TemplateMessageTaskItems> items = templateMessageTaskItemDaoImpl.findAllByTaskId(taskId);
        items.forEach((item) -> {
            commonSendTemplateMessage(task, item);
        });
        templateMessageTaskItemDaoImpl.save(items);
    }

    public void sendTemplateMessage(Long taskId, Long channelId) {
        TemplateMessageTask task = templateMessageTaskDaoImpl.findOne(taskId);

        List<TemplateMessageTaskItems> items = templateMessageTaskItemDaoImpl.findAllByTaskId(taskId);
        items.forEach((item) -> {
            commonSendTemplateMessage(task, item, channelId);
        });
        templateMessageTaskItemDaoImpl.save(items);
    }

    private void commonSendTemplateMessage(TemplateMessageTask task, TemplateMessageTaskItems item, Long... extParams) {
        if (extParams != null) {
            WechatPublicNo channel = pubNoServiceImpl.findOneById(extParams[0]);
            if (channel == null) {
                throw new ServiceWarn(ExceptionConstants.WECHAT_PUBLIC_NO_NOT_EXIST);
            }
            this.appId = channel.getAuthorizerAppid();
        }

        TemplateMessage message = new TemplateMessage();
        if (task.getRedirectUrl() != null && StringUtils.isNotEmpty(task.getRedirectUrl())) {
            message.setUrl(task.getRedirectUrl());
        }
        message.setTouser(item.getOpenid());
        message.setTemplate_id(task.getTemplateId());
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem(task.getFirst(), "#173177", data, "first");
        if (task.getTaskKeywordsLength() >= 1) {
            generateTemplateMessageItem(item.getKeyword1(), "#173177", data, task.getKeyword1name());
        }
        if (task.getTaskKeywordsLength() >= 2) {
            generateTemplateMessageItem(item.getKeyword2(), "#173177", data, task.getKeyword2name());
        }
        if (task.getTaskKeywordsLength() >= 3) {
            generateTemplateMessageItem(item.getKeyword3(), "#173177", data, task.getKeyword3name());
        }
        if (task.getTaskKeywordsLength() >= 4) {
            generateTemplateMessageItem(item.getKeyword4(), "#173177", data, task.getKeyword4name());
        }
        generateTemplateMessageItem(task.getRemark(), "#173177", data, "remark");
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
        if (result.isSuccess()) {
            item.setSendDate(new Date());
            item.setStatus("send success");
        } else {
            item.setSendDate(new Date());
            item.setStatus(result.getErrcode() + "--" + result.getErrmsg());
        }
    }

    public void commonSendTemplateMessagePreview(TemplateMessageTaskItems item) {
        TemplateMessageTask task = templateMessageTaskDaoImpl.findOne(item.getTaskId());
        commonSendTemplateMessage(task, item);
    }

    public RewardsRedeemLog sendRedeemRewardsTemplateMessageForMerklechina(Follower follower,
            RewardsRedeemLog redeemLog) {
        TemplateMessage message = new TemplateMessage();
        message.setTouser(follower.getOpenid());
        message.setTemplate_id("am-HKX4gC4U2Bo5l-NynS2Q9VkLlet6kD92FHayY8wU");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem("兑换奖品通知", "#173177", data, "first");
        generateTemplateMessageItem(redeemLog.getRewardName(), "#173177", data, "keyword1");
        generateTemplateMessageItem(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "#173177", data,
                "keyword2");
        generateTemplateMessageItem("感谢您的参与，祝生活愉快！\n" + "得捷电子", "#173177", data, "remark");
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId()), message);
        redeemLog.setTemplateMessageSendStatus(result.isSuccess());
        if (!result.isSuccess()) {
            redeemLog.setTemplateMessageErrorMessage(result.getErrcode() + "--" + result.getErrmsg());
        }
        return redeemLog;
    }

    public CampaignOfflineCheckInLog sendOfflineCheckInSuccessMessage(Follower follower,
            CampaignOfflineCheckInLog log) {
        TemplateMessage message = new TemplateMessage();
        message.setTouser(follower.getOpenid());
        message.setTemplate_id("W8YgC_u6Lpwkwxc-bNaj_OUGbD2wckGrLz55ZpD38RU");
        message.setUrl("https://lp-cn1-wechat-production.merklechina.com/h5/digikey/member/censample?channel=7&openid="
                + follower.getOpenid());
        // message.setTemplate_id("sVHQ4-w0XldG0YP1i72ErM5f9FLxnO3jDOIgMaV8Zzk");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem("恭喜您签到成功，获得500积分！\n", "#173177", data, "first");
        generateTemplateMessageItem(log.getCampaignTitle() + "签到", "#173177", data, "keyword1");
        generateTemplateMessageItem(follower.getNickname(), "#173177", data, "keyword2");
        generateTemplateMessageItem(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "#173177", data,
                "keyword3");
        generateTemplateMessageItem("感谢您的参与，祝生活愉快！\n" + "得捷电子", "#173177", data, "remark");
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId()), message);
        log.setTemplateMessageSendStatus(result.isSuccess());
        if (!result.isSuccess()) {
            log.setTemplateErrorMessage(result.getErrcode() + "--" + result.getErrmsg());
        }
        return log;
    }
}
