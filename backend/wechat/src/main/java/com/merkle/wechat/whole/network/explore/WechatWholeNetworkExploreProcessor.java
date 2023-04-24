package com.merkle.wechat.whole.network.explore;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.util.XMLUtil;
import com.merkle.wechat.component.WechatMessageParser;
import com.merkle.wechat.config.ThirdPartyPlatformConfig;
import com.merkle.wechat.service.ThirdPartyService;
import com.merkle.wechat.service.TokenService;

import weixin.popular.api.ComponentAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.component.ApiQueryAuthResult;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.bean.xmlmessage.XMLTextMessage;

@Component
public class WechatWholeNetworkExploreProcessor {
    private static final String TESTCOMPONENT_MSG_TYPE_TEXT = "TESTCOMPONENT_MSG_TYPE_TEXT";
    private static final String QUERY_AUTH_CODE_PRE_FIX = "QUERY_AUTH_CODE:";

    /**
     * 微信全网接入测试
     */
    public void process(String messageStr, String appid, HttpServletResponse resp, Map<String, String> params)
            throws Exception {
        EventMessage message = messageParser.parseMessage(messageStr, appid);
        if ("event".equals(message.getMsgType())) {
            processEventMessage(message, resp, params);
            System.out.println("++++++++++++++++++++after process Event Message++++++++++++++++++");
        }
        if ("text".equals(message.getMsgType())) {
            processTextMessage(message, resp, params);
            processCustomerServiceMessage(message, resp, params);
            System.out.println("++++++++++++++++++++after process Text Message++++++++++++++++++");
        }
    }

    private @Autowired WechatMessageParser messageParser;
    private @Autowired ThirdPartyPlatformConfig ttpConfig;

    /**
     * 微信全网接入测试, 事件消息
     */
    private void processEventMessage(EventMessage interactEvent, HttpServletResponse resp, Map<String, String> params) {
        XMLMessage xmlText = new XMLTextMessage(interactEvent.getFromUserName(), interactEvent.getToUserName(),
                TESTCOMPONENT_MSG_TYPE_TEXT + "from_callback");
        String xml = xmlText.toXML();
        String decryptMessage = messageParser.decryptMessage(xml, params, ttpConfig.getCryptArgs());
        try {
            outputStreamWrite(resp.getOutputStream(), decryptMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信全网接入测试, 文本消息
     */
    private void processTextMessage(EventMessage interactMessage, HttpServletResponse resp,
            Map<String, String> params) {
        if (TESTCOMPONENT_MSG_TYPE_TEXT.equals(interactMessage.getContent())) {
            XMLMessage xmlText = new XMLTextMessage(interactMessage.getFromUserName(), interactMessage.getToUserName(),
                    TESTCOMPONENT_MSG_TYPE_TEXT + "_callback");
            String xml = xmlText.toXML();
            String decryptMessage = messageParser.decryptMessage(xml, params, ttpConfig.getCryptArgs());
            try {
                outputStreamWrite(resp.getOutputStream(), decryptMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private @Autowired ThirdPartyService ttpServiceImpl;
    private @Autowired TokenService tokenServiceImpl;

    /**
     * 微信全网接入测试, 客服消息
     */
    private void processCustomerServiceMessage(EventMessage interactMessage, HttpServletResponse resp,
            Map<String, String> params) {
        String content = interactMessage.getContent();
        if (content != null && content.startsWith(QUERY_AUTH_CODE_PRE_FIX)) {
            String queryAuthCode = content.substring(QUERY_AUTH_CODE_PRE_FIX.length());
            // 返回空串表明暂时不回复
            try {
                outputStreamWrite(resp.getOutputStream(), "");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 使用授权码换取公众号的授权信息
            ApiQueryAuthResult basicResult = ComponentAPI.api_query_auth(tokenServiceImpl.getComponentAccessToken(),
                    ttpServiceImpl.getThirdPartyAppId(), queryAuthCode);
            Authorization_info basicInfo = basicResult.getAuthorization_info();
            Message message = new TextMessage(interactMessage.getFromUserName(), queryAuthCode + "_from_api");
            MessageAPI.messageCustomSend(basicInfo.getAuthorizer_access_token(), message);
        }
    }

    private boolean outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
            outputStream.flush();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isTestAppId(String appId) {
        return appId.equals("wx570bc396a51b8ff8");
    }

    public boolean isTestAppName(String appName) {
        return "gh_3c884a361561".equals(appName);
    }

    public boolean isTestApp(String appId, String messageStr) {
        if (!isTestAppId(appId)) {
            return false;
        }

        Document document = XMLUtil.getDocument(messageStr);
        String toUser = XMLUtil.getNodeContent(document, "ToUserName");
        return isTestAppName(toUser);
    }
}
