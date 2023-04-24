package com.merkle.wechat.service.sms;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.merkle.wechat.common.constant.CaptchaStatusConstant;
import com.merkle.wechat.common.dao.AliCaptchaDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.AliCaptcha;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.CaptchaVerifyVo;

@Component
public class AliSmsServiceImpl implements SmsService {
    private @Autowired AliCaptchaDao captchaDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;

    private static int EXPIRED_GAP = 5 * 60 * 1000; // ms

    @Value("${aliyun.sms.accessKey}")
    private String accessKey;
    @Value("${aliyun.sms.accessSecret}")
    private String accessSecret;
    @Value("${aliyun.sms.captcha.template}")
    private String captchaTemplate;
    @Value("${aliyun.sms.captcha.sign}")
    private String captchaSign;

    @Override
    @Transactional
    public String sendCaptcha(String phone, String openid, String appId) throws Exception {
        matchCondition(openid, phone);

        AliCaptcha captcha = new AliCaptcha();
        captcha.setCode(generateSixRandomNumber());
        captcha.setPhone(phone);
        captcha.setOpenid(openid);
        captcha.setExpiredTime(new Date(System.currentTimeMillis() + EXPIRED_GAP));
        captcha.setStatus(CaptchaStatusConstant.UNUSED);
        captcha.setAppId(appId);

        JSONObject resp = sendSms(captcha.getCode(), captcha.getPhone(), captchaTemplate, captchaSign);

        if (resp == null) {
            throw new ServiceWarn(ExceptionConstants.UN_KNOWN_ERROR);
        }
        captcha.setRespCode(resp.getString("Code"));
        captcha.setMessage(resp.getString("Message"));
        captcha.setRequestId(resp.getString("RequestId"));
        captcha.setBizId(resp.getString("BizId"));
        captcha = captchaDaoImpl.save(captcha);
        return captcha.getRespCode();
    }

    private void matchCondition(String openid, String phone) {
        List<AliCaptcha> oldCaptchas = captchaDaoImpl.findByOpenidOrderByCreatedDateDesc(openid);
        if (oldCaptchas != null) {
            if (oldCaptchas.size() > 0) {
                AliCaptcha latestCaptcha = oldCaptchas.get(0);
                boolean sixtySecondsGapLimit = latestCaptcha.getCreatedDate()
                        .after(new Date(System.currentTimeMillis() - 60000));
                // must have 60 seconds gap
                if (sixtySecondsGapLimit) {
                    throw new ServiceWarn(ExceptionConstants.CAPTCHA_SIXTY_SECONDS_LIMIT);
                }
            }
        }

        // must be follower!
        Optional.ofNullable(followerDaoImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));

    }

    private String generateSixRandomNumber() throws Exception {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    @Transactional
    public void verifyCaptcha(CaptchaVerifyVo vo) throws Exception {
        AliCaptcha cap = captchaDaoImpl.findLastByPhoneAndCodeOrderByCreatedDateAsc(vo.getPhone(), vo.getCode());
        verifyCaptcha(cap);
        cap.setStatus(CaptchaStatusConstant.USED);
        captchaDaoImpl.save(cap);
    }

    private void verifyCaptcha(AliCaptcha cap) throws Exception {
        if (cap == null) {
            throw new ServiceWarn(ExceptionConstants.CREDENTIAL_NOT_MATCH_ERROR);
        } else if (cap.getStatus().equals(CaptchaStatusConstant.USED)) {
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_ALREADY_USED_ERROR);
        } else if (cap.getStatus().equals(CaptchaStatusConstant.EXPIRED)) {
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_EXPIRED_ERROR);
        } else if (cap.getExpiredTime().before(new Date())) {
            cap.setStatus(CaptchaStatusConstant.EXPIRED);
            captchaDaoImpl.save(cap);
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_EXPIRED_ERROR);
        }
    }

    public JSONObject sendSms(String code, String phoneNumber, String templateCode, String sign) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setAction("SendSms");
        request.setVersion("2017-05-25");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("SignName", sign);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            return new JSONObject(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return null;
    }

}
