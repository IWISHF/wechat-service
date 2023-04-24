package com.merkle.wechat.handler;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.batch.BatchTaskResultDao;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.batch.BatchTaskResult;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.service.AutoReplyService;
import com.merkle.wechat.service.PreviewQrcodeService;
import com.merkle.wechat.service.PreviewQrcodeServiceImpl;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.statistics.FollowerStatisticsService;

@Component(value = "DefaultMessageHandler")
public class DefaultMessageHandler implements MessageHandler {

    protected Logger logger = LoggerFactory.getLogger("DefaultMessageHandler");

    private @Autowired FollowerService followerServiceImpl;
    private @Autowired AutoReplyService autoReplyServiceImpl;
    private @Autowired BatchTaskResultDao batchTaskResultDaoImpl;
    private @Autowired PreviewQrcodeService previewQrcodeServiceImpl;
    private @Autowired FollowerStatisticsService followerStatisticsServiceImpl;

    public void handleTextMessage(EventMessage message) throws Exception {
        autoReplyServiceImpl.replyTextMessage(message);
    }

    public void handleLinkMessage(EventMessage message) throws Exception {
    }

    public void handleSubscribeEvent(EventMessage event) throws Exception {
        Follower follower = followerServiceImpl.findOrCreateFollower(event.getFromUserName(), event.getAppId());
        boolean isNewFollower = true;
        if (follower.getSubscribe() == 0) {
            followerServiceImpl.syncLatestFollowerInfo(follower);
            isNewFollower = false;
        }

        // Async run
        followerStatisticsServiceImpl.createSubscribe(event);

        // qrcode scan
        if (!StringUtils.isEmpty(event.getEventKey()) && !event.getEventKey().contains(PreviewQrcodeServiceImpl.PREVIEW_PREFIX)) {
            autoReplyServiceImpl.replySubscribeScanEvent(event, isNewFollower);
        } else {
            autoReplyServiceImpl.relpySubscribeEvent(event, isNewFollower);
        }
        // Preview qrcode scan
        if (event.getEventKey() != null && event.getEventKey().contains(PreviewQrcodeServiceImpl.PREVIEW_PREFIX)) {
            previewQrcodeServiceImpl.replySubscribeEvent(event);
        }
    }

    public void handleUnSubscribeEvent(EventMessage event) throws Exception {
        followerServiceImpl.unsubscribeFollower(event.getFromUserName());
        followerStatisticsServiceImpl.createUnsubscribe(event);
    }

    public void handleClickEvent(EventMessage event) throws Exception {
        autoReplyServiceImpl.replyClickEvent(event);
    }

    public void handleScanEvent(EventMessage event) throws Exception {
        if (event.getEventKey() != null && !event.getEventKey().contains(PreviewQrcodeServiceImpl.PREVIEW_PREFIX)) {
            autoReplyServiceImpl.replyScanEvent(event);
        }

        if (event.getEventKey() != null && event.getEventKey().contains(PreviewQrcodeServiceImpl.PREVIEW_PREFIX)) {
            previewQrcodeServiceImpl.replyScanEvent(event);
        }
    }

    @Override
    public void handleMessageJobFinished(EventMessage message) {
        String msgId = message.getMsgId().toString();
        BatchTaskResult data = Optional
                .ofNullable(batchTaskResultDaoImpl.findOneByMsgIdAndToUserName(msgId, message.getToUserName()))
                .orElse(new BatchTaskResult());
        data.setMsgId(msgId);
        data.setToUserName(message.getToUserName());
        data.setStatus(message.getStatus());
        data.setTotalCount(message.getTotalCount());
        data.setFilterCount(message.getFilterCount());
        data.setSentCount(message.getSentCount());
        data.setErrorCount(message.getErrorCount());
        data.setFinished(true);
        data.setUpdatedDate(new Date());
        batchTaskResultDaoImpl.save(data);
    }

    public void handleScanCodePushEvent(EventMessage event) throws Exception {
    }

    public void handleScanCodeWaitingMsgEvent(EventMessage event) throws Exception {

    }

    public void handleViewEvent(EventMessage event) throws Exception {
    }

    public void handleImageMessage(EventMessage message) throws Exception {
    }

    public void handleVoiceMessage(EventMessage message) throws Exception {
    }

    public void handleVideoMessage(EventMessage message) throws Exception {
    }

    public void handleShortVideoMessage(EventMessage message) throws Exception {
    }

    public void handleMsgLocationMessage(EventMessage message) throws Exception {
    }

    public void handleEventLocationEvent(EventMessage event) throws Exception {
    }

}
