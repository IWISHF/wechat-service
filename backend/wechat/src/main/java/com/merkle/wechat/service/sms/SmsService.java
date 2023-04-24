package com.merkle.wechat.service.sms;

import com.merkle.wechat.vo.CaptchaVerifyVo;

public interface SmsService {

    String sendCaptcha(String phone, String openid, String appId) throws Exception;

    void verifyCaptcha(CaptchaVerifyVo vo) throws Exception;

}
