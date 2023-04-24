package com.merkle.wechat.modules.digikey.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.MessageDao;
import com.merkle.wechat.common.dao.batch.BatchTaskDao;
import com.merkle.wechat.common.dao.digikey.SendEMIRecordRepository;
import com.merkle.wechat.common.entity.batch.BatchTask;
import com.merkle.wechat.common.entity.digikey.SendEMIRecord;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.follower.FollowerServiceImpl;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.MessageSendResult;
import weixin.popular.bean.message.massmessage.MassMPnewsMessage;
import weixin.popular.bean.message.massmessage.MassMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.NewsMessage.Article;
import weixin.popular.bean.message.preview.MpnewsPreview;
import weixin.popular.bean.message.preview.Preview;

@Component
public class DigikeyBatchSendServiceImpl {
    protected Logger logger = LoggerFactory.getLogger("DigikeyBatchSendServiceImpl");
    private @Autowired FollowerServiceImpl followerServiceImpl;
    private @Autowired MessageDao messageDao;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired BatchTaskDao batchMessageExecutionTaskDaoImpl;
    // DIGIKEY APPID
    @SuppressWarnings("unused")
    private String digikeyAppId = "wx0a1ea68570a894d5";

    public void send() {
        // need set to digikey appId
        logger.info("========================= DIGIKEY MUNIHEI BATCH SEND START =========================");
        String appId = "wx0a1ea68570a894d5";
        // mpnews mediaId
        String mpNewsMediaId = "ll5LKBAw0iu8FMsswWYraQJsuHgKctKSQQkKmg5tgv4";
        Set<String> muniheiKeywordInvolvedOpenIds = messageDao.getDistinctEventMessageForDigikeyMunihei(appId);
        Set<String> shanghaiFollwerOpenIds = followerServiceImpl.findDistinctOpenIdsByCondition(appId, "上海");
        shanghaiFollwerOpenIds.addAll(muniheiKeywordInvolvedOpenIds);
        logger.info("========================= DIGIKEY MUNIHEI BATCH TOTAL COUNT " + shanghaiFollwerOpenIds.size()
                + " =========================");
        MassMessage message = new MassMPnewsMessage(mpNewsMediaId);
        // TODO:10000上限此处没考虑
        message.setTouser(shanghaiFollwerOpenIds);
        MessageSendResult submitResult = MessageAPI
                .messageMassSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
        if (submitResult.isSuccess()) {
            BatchTask result = new BatchTask();
            // result.setMsgDataId(submitResult.getMsg_data_id() + "");
            // result.setMsgId(submitResult.getMsg_id());
            // result.setMsgStatus(submitResult.getMsg_status());
            // result.setType(submitResult.getType());
            batchMessageExecutionTaskDaoImpl.save(result);
            logger.info(
                    "========================= DIGIKEY MUNIHEI BATCH SEND SUBMMIT TASK TO WECHAT SUCCESS =========================");
        } else {
            throw new ServiceWarn(submitResult.getErrmsg(), submitResult.getErrcode());
        }
    }

    public void sendPreview(String openId) {
        // need set to digikey appId wx0a1ea68570a894d5
        logger.info("========================= DIGIKEY MUNIHEI BATCH SEND START =========================");
        String appId = "wx0a1ea68570a894d5";
        // mpnews mediaId
        String mpNewsMediaId = "ll5LKBAw0iu8FMsswWYraQJsuHgKctKSQQkKmg5tgv4";

        Set<String> muniheiKeywordInvolvedOpenIds = messageDao.getDistinctEventMessageForDigikeyMunihei(appId);
        Set<String> shanghaiFollwerOpenIds = followerServiceImpl.findDistinctOpenIdsByCondition(appId, "上海");
        shanghaiFollwerOpenIds.addAll(muniheiKeywordInvolvedOpenIds);
        logger.info("========================= DIGIKEY MUNIHEI BATCH TOTAL COUNT " + shanghaiFollwerOpenIds.size()
                + " =========================");
        Preview preview = new MpnewsPreview(mpNewsMediaId);
        preview.setTouser(openId);
        MessageSendResult previewResult = MessageAPI
                .messageMassPreview(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), preview);
        if (previewResult.isSuccess()) {
            BatchTask result = new BatchTask();
            // result.setMsgDataId(previewResult.getMsg_data_id() + "");
            // result.setMsgId(previewResult.getMsg_id());
            // result.setMsgStatus(previewResult.getMsg_status());
            // result.setType(previewResult.getType());
            batchMessageExecutionTaskDaoImpl.save(result);
            logger.info(
                    "========================= DIGIKEY MUNIHEI BATCH SEND SUBMMIT TASK TO WECHAT SUCCESS =========================");
        } else {
            throw new ServiceWarn(previewResult.getErrmsg(), previewResult.getErrcode());
        }
    }

    private @Autowired SendEMIRecordRepository sendEMIRecordRepositoryImpl;

    public void sendEMIArticle() {
        String appId = "wx0a1ea68570a894d5";
        logger.info("========================= DIGIKEY EMI ARTICLE SEND START =========================");
        Set<String> muniheiKeywordInvolvedOpenIds = messageDao.getDistinctEventMessageForDigikeyMunihei(appId);
        Set<String> shanghaiFollwerOpenIds = followerServiceImpl.findDistinctOpenIdsByCondition(appId, "上海");
        shanghaiFollwerOpenIds.addAll(muniheiKeywordInvolvedOpenIds);
        logger.info("========================= DIGIKEY EMI ARTICLE SEND TOTAL COUNT " + shanghaiFollwerOpenIds.size()
                + " =========================");
        int total = shanghaiFollwerOpenIds.size();
        int success = 0;
        for (String openid : shanghaiFollwerOpenIds) {
            SendEMIRecord sendEMIRecord = new SendEMIRecord();
            sendEMIRecord.setOpenid(openid);
            try {
                Message picMessage = new NewsMessage(openid, generateArticleListForEMI());
                BaseResult result = MessageAPI.messageCustomSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId),
                        picMessage);
                if (result.isSuccess()) {
                    success++;
                    sendEMIRecord.setStatus(true);
                } else {
                    logger.info("===ErrorCode: " + result.getErrcode() + "message: " + result.getErrmsg());
                    sendEMIRecord.setStatus(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendEMIRecord.setStatus(false);
            } finally {
                sendEMIRecordRepositoryImpl.save(sendEMIRecord);
            }
        }
        logger.info("========================= DIGIKEY EMI ARTICLE SEND END TOTAL " + total + " SUCCESS: " + success
                + " FAILED: " + (total - success) + " =========================");
    }

    private List<Article> generateArticleListForEMI() {
        List<Article> articleList = new ArrayList<>();
        Article article = new Article();
        article.setTitle("EMI/噪声抑制如何做？专家有妙招儿 | 大咖在DK第1期");
        article.setDescription("本集《大咖在DK》访谈中，来自KEMET的专家将重点讨论KEMET- FLEX SUPPRESSOR Sheets, EMI/噪声柔性抑制抑制片材。");
        article.setPicurl(
                "http://mmbiz.qpic.cn/mmbiz_jpg/ib8wcw6sXJMiaEKnHrXuwvc9jc1s4Ua9cY9B91LkuvYs5B1jXMbIrtic37ERUupzNys5SrGOJFcOPQFaMsBnrVkdQ/0?wx_fmt=jpeg");
        article.setUrl(
                "http://mp.weixin.qq.com/s?__biz=MzI4MzM5MzIwNA==&mid=100002532&idx=1&sn=06f0701e6fa9a802a3f8c978275ea6b8&chksm=6b8a273f5cfdae29f8c88c6921f1100a121cdb37751c1c18a076f282a9626f6e7b0ba1a7fad4#rd");
        articleList.add(article);
        return articleList;
    }
    
}
