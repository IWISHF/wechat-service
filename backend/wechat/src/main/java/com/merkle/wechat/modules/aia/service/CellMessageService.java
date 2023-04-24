package com.merkle.wechat.modules.aia.service;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.modules.aia.vo.AIAUserInfoVo;
import com.merkle.wechat.modules.aia.vo.GetUserInfoVo;

public interface CellMessageService {

    void sendCaptcha(String phone, String openid, Long channelId) throws Exception;

    void verifyCaptchaAndCreateAIAUserInfo(AIAUserInfoVo vo, WechatPublicNo pbNo) throws Exception;

    GetUserInfoVo getAIAUserInfo(String openid, WechatPublicNo pbNo);

}
