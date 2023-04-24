package com.merkle.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.PreviewQrcodeDao;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.PreviewQrcode;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;

import weixin.popular.api.MessageAPI;
import weixin.popular.api.QrcodeAPI;
import weixin.popular.bean.message.preview.ImagePreview;
import weixin.popular.bean.message.preview.MpnewsPreview;
import weixin.popular.bean.message.preview.MpvideoPreview;
import weixin.popular.bean.message.preview.Preview;
import weixin.popular.bean.message.preview.TextPreview;
import weixin.popular.bean.qrcode.QrcodeTicket;

@Component
public class PreviewQrcodeServiceImpl implements PreviewQrcodeService {
    private @Autowired PreviewQrcodeDao previewQrcodeDaoImpl;
    private @Autowired TokenService tokenServiceImpl;
    private final int MAX_EXPIRE_SECONDS = 1800;
    public static final String PREVIEW_PREFIX = "QRPREVIEW";

    @Override
    public String createPreviewBatchTask(WechatPublicNo pbNo, AutoReplyRule rule) {
        String sceneStr = PREVIEW_PREFIX + pbNo.getId() + System.currentTimeMillis();
        QrcodeTicket ticket = QrcodeAPI.qrcodeCreateTemp(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), MAX_EXPIRE_SECONDS,
                sceneStr);
        if (ticket.isSuccess()) {
            PreviewQrcode code = PreviewQrcode.convertFromTicket(ticket);
            code.setWechatPublicNoId(pbNo.getId());
            code.setAppId(pbNo.getAuthorizerAppid());
            code.setSceneContent(sceneStr);
            code.setType("tempSceneStr");
            code.setAutoReplyrule(rule);
            code.setDescription("preview Batch task");
            code.setName("preview Batch Task");
            code.setPreviewType(PreviewQrcode.PREVIEW_BATCH_TASK);
            code.setToUserName(pbNo.getUserName());
            previewQrcodeDaoImpl.save(code);
            return ticket.getTicket();
        } else {
            throw new ServiceWarn(ticket.getErrmsg(), ticket.getErrcode());
        }
    }

    @Override
    public void replySubscribeEvent(EventMessage event) {
        replyScanEvent(event);

    }

    @Override
    public void replyScanEvent(EventMessage event) {
        List<PreviewQrcode> qrcodes = previewQrcodeDaoImpl.findByToUserName(event.getToUserName());
        if (qrcodes == null || qrcodes.size() == 0) {
            return;
        }
        for (PreviewQrcode qr : qrcodes) {
            String sceneContent = qr.getSceneContent();
            if (event.getEventKey() != null && event.getEventKey().contains(sceneContent)) {
                replyScanEvent(event, qr);
                break;
            }
        }
    }

    private void replyScanEvent(EventMessage event, PreviewQrcode qr) {
        AutoReplyRule rule = qr.getAutoReplyrule();
        if (rule == null) {
            return;
        }
        Preview preview = generatePreviewMessage(rule, event.getFromUserName());
        if (preview == null) {
            return;
        }
        MessageAPI.messageMassPreview(tokenServiceImpl.getPublicNoAccessTokenByAppId(qr.getAppId()), preview);
    }

    private Preview generatePreviewMessage(AutoReplyRule rule, String toUser) {
        switch (rule.getReplyType()) {
            case AutoReplyRule.REPLY_TEXT: {
                TextPreview message = new TextPreview(rule.getReplyTexts());
                message.setTouser(toUser);
                return message;
            }
            case AutoReplyRule.REPLY_PICTURE: {
                ImagePreview message = new ImagePreview(rule.getMediaId());
                message.setTouser(toUser);
                return message;
            }
            case AutoReplyRule.REPLY_VIDEO: {
                MpvideoPreview message = new MpvideoPreview(rule.getMediaId());
                message.setTouser(toUser);
                return message;
            }
            case AutoReplyRule.REPLY_MPNEWS: {
                MpnewsPreview message = new MpnewsPreview(rule.getMediaId());
                message.setTouser(toUser);
                return message;
            }
        }
        return null;
    }

}
