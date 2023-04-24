package com.merkle.wechat.component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.exception.ServiceError;
import com.merkle.wechat.common.util.JSONBuilder;
import com.merkle.wechat.vo.thridparty.ThirdPartyPlatformCryptArgs;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

public abstract class MessageParser {

    protected Logger logger = LoggerFactory.getLogger("MessageParser");

    /**
     * Validate the message's origin/sender
     * 
     * @param params
     * @param account
     * @return
     */
    public String verifyUrl(Map<String, String> params, ThirdPartyPlatformCryptArgs account) {
        logger.info(" ###### WeChat validateMessage params: " + params);

        String signature = params.get("msg_signature");
        if (signature == null) {
            signature = params.get("signature");
        }
        String timeStamp = params.get("timestamp");
        String nonce = params.get("nonce");
        String echostr = params.get("echostr");

        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(account.getToken(), account.getEncodingAESKey(),
                    account.getEncryptAppId());
            return wxBizMsgCrypt.verifyUrl(signature, timeStamp, nonce, echostr);
        } catch (AesException e) {
            handleAesException(null, params, account, e);
        }
        return null;
    }

    protected void handleAesException(String postBody, Map<String, String> params, ThirdPartyPlatformCryptArgs account,
            AesException e) {
        JSONBuilder errorParams = JSONBuilder.object("appid", account.getEncryptAppId()).putAll(params);
        if (postBody != null) {
            errorParams.put("postdata", postBody);
        }
        throw new ServiceError("Decrypt msg failed, cause by AesException", e, Arrays.asList(errorParams));
    }

    @SuppressWarnings({ "unchecked" })
    protected Map<String, Object> filterEmptyElement(JSON json) {
        Map<String, Object> extra = (Map<String, Object>) json;
        extra = extra.entrySet().stream().filter(entry -> {
            Object value = entry.getValue();
            if (value == null || value instanceof JSONArray && ((JSONArray) value).isEmpty()) {
                return false;
            }
            return true;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return extra;
    }

    /**
     * Encrypt message
     * 
     * @param message
     * @param params
     * @param account
     * @return
     */
    public abstract String encryptMessage(String message, Map<String, String> params,
            ThirdPartyPlatformCryptArgs account);

    /**
     * Decrypt message
     * 
     * @param message
     * @param params
     * @param account
     * @return
     */
    public abstract String decryptMessage(String message, Map<String, String> params,
            ThirdPartyPlatformCryptArgs account);

    public EventMessage parseMessage(String messageStr, String publicNoAppId) throws Exception {
        return null;
    }

}