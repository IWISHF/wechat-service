package com.merkle.wechat.component;

import static com.merkle.wechat.constant.Constants.CLICK;
import static com.merkle.wechat.constant.Constants.EVENT_ERRORCOUNT;
import static com.merkle.wechat.constant.Constants.EVENT_FILTERCOUNT;
import static com.merkle.wechat.constant.Constants.EVENT_KEY;
import static com.merkle.wechat.constant.Constants.EVENT_LATITUDE;
import static com.merkle.wechat.constant.Constants.EVENT_LOCATION;
import static com.merkle.wechat.constant.Constants.EVENT_LONGITUDE;
import static com.merkle.wechat.constant.Constants.EVENT_PRECISION;
import static com.merkle.wechat.constant.Constants.EVENT_SCAN_CODE_INFO;
import static com.merkle.wechat.constant.Constants.EVENT_SCAN_RESULT;
import static com.merkle.wechat.constant.Constants.EVENT_SCAN_TYPE;
import static com.merkle.wechat.constant.Constants.EVENT_SENTCOUNT;
import static com.merkle.wechat.constant.Constants.EVENT_STATUS;
import static com.merkle.wechat.constant.Constants.EVENT_TICKET;
import static com.merkle.wechat.constant.Constants.EVENT_TOTALCOUNT;
import static com.merkle.wechat.constant.Constants.IMAGE;
import static com.merkle.wechat.constant.Constants.IMAGE_PICTURE_URL;
import static com.merkle.wechat.constant.Constants.LINK;
import static com.merkle.wechat.constant.Constants.LINK_DESCRIPTION;
import static com.merkle.wechat.constant.Constants.LINK_TITLE;
import static com.merkle.wechat.constant.Constants.LINK_URL;
import static com.merkle.wechat.constant.Constants.LOCATION_LABEL;
import static com.merkle.wechat.constant.Constants.LOCATION_LOCATION_X;
import static com.merkle.wechat.constant.Constants.LOCATION_LOCATION_Y;
import static com.merkle.wechat.constant.Constants.LOCATION_SCALE;
import static com.merkle.wechat.constant.Constants.MASSSEND_JOB_FINISH;
import static com.merkle.wechat.constant.Constants.MEDIA_ID;
import static com.merkle.wechat.constant.Constants.MESSAGE_ID;
import static com.merkle.wechat.constant.Constants.MESSAGE_ID_MASSRESULT;
import static com.merkle.wechat.constant.Constants.MSG_LOCATION;
import static com.merkle.wechat.constant.Constants.SCAN;
import static com.merkle.wechat.constant.Constants.SCANCODE_PUSH;
import static com.merkle.wechat.constant.Constants.SCANCODE_WAITMSG;
import static com.merkle.wechat.constant.Constants.SHORT_VIDEO;
import static com.merkle.wechat.constant.Constants.SUBSCRIBE;
import static com.merkle.wechat.constant.Constants.TEXT;
import static com.merkle.wechat.constant.Constants.TEXT_CONTENT;
import static com.merkle.wechat.constant.Constants.UNSUBSCRIBE;
import static com.merkle.wechat.constant.Constants.VIDEO;
import static com.merkle.wechat.constant.Constants.VIDEO_THUMBMEDIA_ID;
import static com.merkle.wechat.constant.Constants.VIEW;
import static com.merkle.wechat.constant.Constants.VOICE;
import static com.merkle.wechat.constant.Constants.VOICE_FORMAT;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.merkle.wechat.common.dao.MessageDao;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.exception.ServiceError;
import com.merkle.wechat.common.util.XMLUtil;
import com.merkle.wechat.constant.Constants;
import com.merkle.wechat.vo.thridparty.ThirdPartyPlatformCryptArgs;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

@Component
public class WechatMessageParser extends MessageParser {
    private @Autowired MessageDao messageDaoImpl;

    public String buildAcknowledgeMessage() {
        return Constants.RESPONSE_SUCCESS;
    }

    @Override
    public String encryptMessage(String message, Map<String, String> params,
            ThirdPartyPlatformCryptArgs wxBizMsgCryptArgs) {
        logger.info(" ###### before encryptParams: " + params);
        logger.info(" ###### before encryptMessage: " + message);

        String encryptType = params.get("encrypt_type");
        if (null == encryptType || "raw".equals(encryptType)) {
            return message;
        }

        String messageStr = message;
        String timeStamp = params.get("timestamp");
        String nonce = params.get("nonce");

        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(wxBizMsgCryptArgs.getToken(),
                    wxBizMsgCryptArgs.getEncodingAESKey(), wxBizMsgCryptArgs.getEncryptAppId());
            messageStr = wxBizMsgCrypt.encryptMsg(messageStr, timeStamp, nonce);
        } catch (AesException e) {
            handleAesException(message, params, wxBizMsgCryptArgs, e);
        }

        logger.info(" ###### after encryptMessage: " + messageStr);
        return messageStr;
    }

    @Override
    public String decryptMessage(String message, Map<String, String> params,
            ThirdPartyPlatformCryptArgs wxBizMsgCryptArgs) {
        logger.info(" ###### before decryptParams: " + params);
        logger.info(" ###### before decryptMessage: " + message);

        String encryptType = params.get("encrypt_type");
        if (null == encryptType || "raw".equals(encryptType)) {
            return message;
        }

        String messageStr = message;
        String msgSignature = params.get("msg_signature");
        String timeStamp = params.get("timestamp");
        String nonce = params.get("nonce");

        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(wxBizMsgCryptArgs.getToken(),
                    wxBizMsgCryptArgs.getEncodingAESKey(), wxBizMsgCryptArgs.getEncryptAppId());
            messageStr = wxBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, messageStr);
        } catch (AesException e) {
            handleAesException(message, params, wxBizMsgCryptArgs, e);
        }

        logger.info(" ###### after decryptMessage: " + messageStr);
        return messageStr;
    }

    @Override
    public EventMessage parseMessage(String messageStr, String publicNoAppId) throws Exception {
        logger.info(" ######======++ before parse message: " + messageStr);
        Document document = XMLUtil.getDocument(messageStr);
        EventMessage message = new EventMessage();

        String fromUserName = XMLUtil.getNodeContent(document, Constants.FROM_USER_NAME);
        Date createTime = parseCreateTime(document);
        // make sure message unique.
        List<EventMessage> msgs = messageDaoImpl.findByFromUserNameAndCreateTime(fromUserName, createTime.getTime());
        if (msgs != null && msgs.size() > 0) {
            logger.info(" ######======++ end parse message repeat message =======");
            return null;
        }

        String toUserName = XMLUtil.getNodeContent(document, Constants.TO_USER_NAME);
        String msgType = XMLUtil.getNodeContent(document, Constants.MESSAGE_TYPE);
        String event = "";

        if (msgType.equals("event")) {
            event = XMLUtil.getNodeContent(document, Constants.EVENT);
        }

        message.setFromUserName(fromUserName);
        message.setToUserName(toUserName);
        message.setCreateTime(createTime.getTime());
        message.setMessageCreatedTime(createTime);
        message.setMsgType(msgType);
        message.setEvent(event);
        message.setAppId(publicNoAppId);
        message = parseMessageData(document, msgType, message, messageStr);
        message = messageDaoImpl.save(message);
        logger.info(" ######======++ end parse message:==event: " + message.getEvent() + "===msgType:" + msgType);
        return message;
    }

    private Date parseCreateTime(Document document) {
        String dateStr = XMLUtil.getNodeContent(document, Constants.CREATE_TIME);
        try {
            Long date = Long.valueOf(dateStr) * 1000;
            return new Date(date);
        } catch (NumberFormatException e) {
            throw new ServiceError(e);
        }
    }

    private EventMessage parseMessageData(Document document, String msgType, EventMessage message, String messageStr)
            throws Exception {
        String typeStr = msgType;
        if (msgType.equals("event")) {
            typeStr = message.getEvent();
        }
        switch (typeStr) {
            /* Message type */
            case TEXT:
                return parseTextMessageData(document, message);
            case IMAGE:
                return parseImageMessageData(document, message);
            case VOICE:
                return parseVoiceMessageData(document, message);
            case VIDEO:
                return parseVideoMessageData(document, message);
            case SHORT_VIDEO:
                return parseShortVideoMessageData(document, message);
            case MSG_LOCATION:
                return parseLocationMessageData(document, message);
            case LINK:
                return parseLinkMessageData(document, message);

            /* Event type */
            case SUBSCRIBE:
                return parseSubscirbeEventData(document, message);
            case UNSUBSCRIBE:

                return message;
            case EVENT_LOCATION:
                return parseLocationEventData(document, message);
            case CLICK:
                message = parseClickEventData(document, message);
                return message;
            case VIEW:
                return parseViewEventData(document, message);
            case SCAN:
                return parseScanCodeEventData(document, message);
            case SCANCODE_PUSH:
                return parseScanCodePushEventData(document, message);
            case SCANCODE_WAITMSG:
                return parseScanCodeWaitMsgEventData(document, message);
            case MASSSEND_JOB_FINISH:
                return parseMassSendJobFinishEventData(document, message);
            default:
                // TODO:
                return message;
        }
    }

    private EventMessage parseMassSendJobFinishEventData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID_MASSRESULT);
        String status = XMLUtil.getNodeContent(document, EVENT_STATUS);
        String totalCount = XMLUtil.getNodeContent(document, EVENT_TOTALCOUNT);
        String filterCount = XMLUtil.getNodeContent(document, EVENT_FILTERCOUNT);
        String sentCount = XMLUtil.getNodeContent(document, EVENT_SENTCOUNT);
        String errorCount = XMLUtil.getNodeContent(document, EVENT_ERRORCOUNT);

        message.setMsgId(Long.valueOf(msgId));
        message.setStatus(status);
        message.setTotalCount(Integer.parseInt(totalCount));
        message.setFilterCount(Integer.parseInt(filterCount));
        message.setSentCount(Integer.parseInt(sentCount));
        message.setErrorCount(Integer.parseInt(errorCount));

        return message;
    }

    private EventMessage parseScanCodePushEventData(Document document, EventMessage message) {
        String eventKey = XMLUtil.getNodeContent(document, EVENT_KEY);
        String scanType = XMLUtil.getNodeContent(document, EVENT_SCAN_TYPE);
        String scanResult = XMLUtil.getNodeContent(document, EVENT_SCAN_RESULT);
        String scanCodeInfo = XMLUtil.getNodeContent(document, EVENT_SCAN_CODE_INFO);

        message.setScanCodeInfo(scanCodeInfo);
        message.setEventKey(eventKey);
        message.setScanType(scanType);
        message.setScanResult(scanResult);

        return message;
    }

    private EventMessage parseScanCodeWaitMsgEventData(Document document, EventMessage message) {
        String eventKey = XMLUtil.getNodeContent(document, EVENT_KEY);
        String scanType = XMLUtil.getNodeContent(document, EVENT_SCAN_TYPE);
        String scanResult = XMLUtil.getNodeContent(document, EVENT_SCAN_RESULT);
        String scanCodeInfo = XMLUtil.getNodeContent(document, EVENT_SCAN_CODE_INFO);

        message.setScanCodeInfo(scanCodeInfo);
        message.setEventKey(eventKey);
        message.setScanType(scanType);
        message.setScanResult(scanResult);

        return message;
    }

    private EventMessage parseClickEventData(Document document, EventMessage message) {
        String eventKey = XMLUtil.getNodeContent(document, EVENT_KEY);

        message.setEventKey(eventKey);

        return message;
    }

    private EventMessage parseViewEventData(Document document, EventMessage message) {
        String eventKey = XMLUtil.getNodeContent(document, EVENT_KEY);

        message.setEventKey(eventKey);

        return message;
    }

    private EventMessage parseScanCodeEventData(Document document, EventMessage message) {
        String eventKey = XMLUtil.getNodeContent(document, EVENT_KEY);
        String ticket = XMLUtil.getNodeContent(document, EVENT_TICKET);

        message.setEventKey(eventKey);
        message.setTicket(ticket);

        return message;
    }

    private EventMessage parseLocationEventData(Document document, EventMessage message) {
        String latitude = XMLUtil.getNodeContent(document, EVENT_LATITUDE);
        String longtitude = XMLUtil.getNodeContent(document, EVENT_LONGITUDE);
        String pricision = XMLUtil.getNodeContent(document, EVENT_PRECISION);
        message.setLatitude(Double.valueOf(latitude));
        message.setLongitude(Double.valueOf(longtitude));
        message.setPrecision(Double.valueOf(pricision));
        return message;
    }

    private EventMessage parseSubscirbeEventData(Document document, EventMessage message) {
        String eventKey = XMLUtil.getNodeContent(document, EVENT_KEY);
        String ticket = XMLUtil.getNodeContent(document, EVENT_TICKET);
        message.setEventKey(eventKey);
        message.setTicket(ticket);
        return message;
    }

    private EventMessage parseTextMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String content = XMLUtil.getNodeContent(document, TEXT_CONTENT);
        message.setContent(content);
        message.setMsgId(Long.valueOf(msgId));

        return message;
    }

    private EventMessage parseImageMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String picUrl = XMLUtil.getNodeContent(document, IMAGE_PICTURE_URL);
        String mediaId = XMLUtil.getNodeContent(document, MEDIA_ID);

        message.setMsgId(Long.valueOf(msgId));
        message.setPicUrl(picUrl);
        message.setMediaId(mediaId);

        return message;
    }

    private EventMessage parseVoiceMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String format = XMLUtil.getNodeContent(document, VOICE_FORMAT);
        String mediaId = XMLUtil.getNodeContent(document, MEDIA_ID);

        message.setMsgId(Long.valueOf(msgId));
        message.setFormat(format);
        message.setMediaId(mediaId);

        return message;
    }

    private EventMessage parseVideoMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String mediaId = XMLUtil.getNodeContent(document, MEDIA_ID);
        String thumbMediaId = XMLUtil.getNodeContent(document, VIDEO_THUMBMEDIA_ID);

        message.setMsgId(Long.valueOf(msgId));
        message.setMediaId(mediaId);
        message.setThumbMediaId(thumbMediaId);

        return message;
    }

    private EventMessage parseShortVideoMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String mediaId = XMLUtil.getNodeContent(document, MEDIA_ID);
        String thumbMediaId = XMLUtil.getNodeContent(document, VIDEO_THUMBMEDIA_ID);

        message.setMsgId(Long.valueOf(msgId));
        message.setMediaId(mediaId);
        message.setThumbMediaId(thumbMediaId);

        return message;
    }

    private EventMessage parseLocationMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String locationX = XMLUtil.getNodeContent(document, LOCATION_LOCATION_X);
        String locationY = XMLUtil.getNodeContent(document, LOCATION_LOCATION_Y);
        String scale = XMLUtil.getNodeContent(document, LOCATION_SCALE);
        String label = XMLUtil.getNodeContent(document, LOCATION_LABEL);

        message.setMsgId(Long.valueOf(msgId));
        message.setLocationX(Double.valueOf(locationX));
        message.setLocationY(Double.valueOf(locationY));
        message.setScale(Double.valueOf(scale));
        message.setLabel(label);

        return message;
    }

    private EventMessage parseLinkMessageData(Document document, EventMessage message) {
        String msgId = XMLUtil.getNodeContent(document, MESSAGE_ID);
        String title = XMLUtil.getNodeContent(document, LINK_TITLE);
        String url = XMLUtil.getNodeContent(document, LINK_URL);
        String description = XMLUtil.getNodeContent(document, LINK_DESCRIPTION);

        message.setMsgId(Long.valueOf(msgId));
        message.setTitle(title);
        message.setUrl(url);
        message.setDescription(description);

        return message;
    }
}
