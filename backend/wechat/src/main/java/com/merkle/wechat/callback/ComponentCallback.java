package com.merkle.wechat.callback;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import com.merkle.wechat.common.dao.ComponentTicketDao;
import com.merkle.wechat.common.entity.ComponentTicket;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.XMLUtil;
import com.merkle.wechat.component.WechatMessageParser;
import com.merkle.wechat.config.ThirdPartyPlatformConfig;
import com.merkle.wechat.constant.Constants;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.consumer.MessageConsumer;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.service.ComponentService;
import com.merkle.wechat.whole.network.explore.WechatWholeNetworkExploreProcessor;

import weixin.popular.util.SignatureUtil;

@RestController
public class ComponentCallback extends AbstractController {
    private static final String INFO_TYPE_COMPONENT_VERIFY_TICKET = "component_verify_ticket";
    private static final String INFO_TYPE_UNAUTHORIZED = "unauthorized";
    private static final String NODE_INFO_TYPE = "InfoType";
    private static final String NODE_COMPONENT_VERIFY_TICKET = "ComponentVerifyTicket";
    private static final String NODE_AUTHORIZER_APP_ID = "AuthorizerAppid";

    protected Logger logger = LoggerFactory.getLogger("ComponentCallback");

    private @Autowired WechatWholeNetworkExploreProcessor processor;

    private @Autowired ComponentService componentServiceImpl;

    private @Autowired WechatMessageParser messageParser;

    private @Autowired ThirdPartyPlatformConfig ttpConfig;

    private @Autowired ComponentTicketDao ttpTicketDaoImpl;

    private @Autowired MessageConsumer messageConsumer;

    @RequestMapping(value = "/wechat/platform/listener", method = RequestMethod.POST)
    public String acceptCallback(@RequestBody String requsetBody, @RequestParam Map<String, String> params) {
        logger.info("----- Receive Wechat Pushing " + requsetBody + "\n" + "Validation Message " + params);

        String messageStr = messageParser.decryptMessage(requsetBody, params, ttpConfig.getCryptArgs());
        Document document = XMLUtil.getDocument(messageStr);
        String infoType = XMLUtil.getNodeContent(document, NODE_INFO_TYPE);

        switch (infoType) {
            case INFO_TYPE_COMPONENT_VERIFY_TICKET:
                String componentVerifyTicket = XMLUtil.getNodeContent(document, NODE_COMPONENT_VERIFY_TICKET);
                ComponentTicket ticket = ttpTicketDaoImpl.findFirstByAppId(ttpConfig.getThirdPartyAppId());
                if (ticket != null) {
                    ticket.setTicket(componentVerifyTicket);
                    ticket.setCreated(new Date());
                } else {
                    ticket = new ComponentTicket();
                    ticket.setAppId(ttpConfig.getThirdPartyAppId());
                    ticket.setTicket(componentVerifyTicket);
                }

                logger.info("componentTicket:" + componentVerifyTicket);
                ttpTicketDaoImpl.save(ticket);

                break;
            case INFO_TYPE_UNAUTHORIZED:
                String appId = XMLUtil.getNodeContent(document, NODE_AUTHORIZER_APP_ID);
                componentServiceImpl.deAuth(appId);
                break;
        }

        return Constants.RESPONSE_SUCCESS;
    }

    @GetMapping("/wechat/test/tps")
    public String test() {
        return "ok";
    }

    @RequestMapping(value = "/wechat/platform/account/{appid}/listener", method = RequestMethod.POST)
    public void callback(@PathVariable String appid, @RequestBody String requsetBody,
            @RequestParam Map<String, String> params, HttpServletResponse response) throws Exception {
        outputStreamWrite(response.getOutputStream(), "success");
        response.getOutputStream().flush();
        response.getOutputStream().close();
        logger.info("+++++ Receive Wechat Pushing for public no" + requsetBody + "\n" + "Validation Message " + params);
        // write back success string.
        String messageStr = messageParser.decryptMessage(requsetBody, params, ttpConfig.getCryptArgs());
        if (processor.isTestApp(appid, messageStr)) {
            processor.process(messageStr, appid, response, params);
            return;
        }
        EventMessage message = messageParser.parseMessage(messageStr, appid);
        messageConsumer.consumeWechatMessage(message);
        logger.info("+++++++++++=== handle success ===========++++++++++++");
    }

    @RequestMapping(value = "/wechat/platform/account/{appid}/listener", method = RequestMethod.GET)
    public void callbackTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        // 首次请求申请验证,返回echostr
        if (echostr != null) {
            outputStreamWrite(outputStream, echostr);
            return;
        }

        // 验证请求签名
        if (!signature.equals(
                SignatureUtil.generateEventMessageSignature(ttpConfig.getThirdPartyToken(), timestamp, nonce))) {
            System.out.println("The request signature is invalid");
            return;
        } else {
            outputStreamWrite(outputStream, echostr);
            return;
        }
    }

    private boolean outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
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

    @RequestMapping(value = "/")
    public String health() {
        return "ok";
    }

    // 公众号授权
    @RequestMapping(value = "/wechat/account/{accountId}/authurl", method = RequestMethod.POST)
    public String wechatAuth(String redirect, @PathVariable Long accountId) {
        if (StringUtils.isEmpty(redirect) || accountId == null) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        String encodeString = Base64.getEncoder().encodeToString(redirect.getBytes());
        String url = componentServiceImpl.buildAuthUrl(accountId) + "/" + encodeString;
        logger.info("+++++++++++get auth url ++++++++++" + url);
        return url;
    }

    @RequestMapping(value = "/wechat/auth/{accountId}/{clientRedirect}")
    public void wechatAuthRedirect(@PathVariable String clientRedirect, @PathVariable Long accountId, String auth_code,
            String expires_in, HttpServletResponse response) throws Exception {
        logger.info("+++++++++++ redirect:" + new String(Base64.getDecoder().decode(clientRedirect)));
        componentServiceImpl.syncPublicNoInfo(auth_code, accountId);
        response.sendRedirect(new String(Base64.getDecoder().decode(clientRedirect)));
    }

}
