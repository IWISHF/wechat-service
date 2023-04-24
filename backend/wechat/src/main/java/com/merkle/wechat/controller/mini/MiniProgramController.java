package com.merkle.wechat.controller.mini;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.service.MiniProgramService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.sms.SmsService;
import com.merkle.wechat.util.JwtMiniProgramUtil;
import com.merkle.wechat.vo.CaptchaVerifyVo;
import com.merkle.wechat.vo.mini.DecryptDataVo;
import com.merkle.wechat.vo.mini.MiniBindBasicInfoVo;
import com.merkle.wechat.vo.mini.MiniFollowerBasicInfoVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "小程序")
@RequestMapping("/wechat/mini/{appId}")
public class MiniProgramController extends AbstractController {
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired MiniProgramService miniProgramServiceImpl;
    private @Autowired SmsService smsServiceImpl;

    @NeedWrap
    @GetMapping("/login")
    @ApiOperation("小程序登录")
    public void miniLogin(@Valid @NotEmpty @RequestParam String code, @PathVariable String appId,
            HttpServletResponse response) {
        WechatPublicNo mini = Optional.ofNullable(pbNoServiceImpl.findOneByAuthorizerAppid(appId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        Follower f = miniProgramServiceImpl.code2Session(code, mini);
        String authToken = JwtMiniProgramUtil.generateToken(f.getOpenid());
        response.setHeader("x-auth-mini-token", authToken);
        response.setHeader("Access-Control-Expose-Headers", "x-auth-mini-token");
    }

    @NeedWrap
    @GetMapping("/basic")
    @ApiOperation("小程序用户基本信息")
    public MiniFollowerBasicInfoVo basicInfo(@PathVariable String appId) {
        String openid = retrieveUserOpenid();
        return miniProgramServiceImpl.getBasicInfo(appId, openid);
    }

    @NeedWrap
    @PostMapping("/basic/bind")
    @ApiOperation("小程序用户基本信息")
    public String bindBasicInfo(@RequestBody MiniBindBasicInfoVo vo, @PathVariable String appId) throws Exception {
        String openid = retrieveUserOpenid();
        return miniProgramServiceImpl.bindBasicInfo(appId, openid, vo);
    }

    @NeedWrap
    @PostMapping("/decrypt")
    @ApiOperation("小程序数据解密")
    public String decryptMiniData(@RequestBody DecryptDataVo data, @PathVariable String appId) throws Exception {
        String openid = retrieveUserOpenid();
        return miniProgramServiceImpl.decryptMiniData(appId, openid, data);
    }

    @NeedWrap
    @GetMapping("/sms/captcha")
    @ApiOperation("小程序发送验证码")
    public String sendMiniCaptcha(@RequestParam(required = true) String phone, @PathVariable String appId)
            throws Exception {
        String openid = retrieveUserOpenid();
        String resp = smsServiceImpl.sendCaptcha(phone, openid, appId);
        if (!resp.equals("OK")) {
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_ERROR);
        }
        return "ok";
    }

    @NeedWrap
    @PostMapping("/sms/captcha/verify")
    @ApiOperation("小程序验证码校验")
    public String verifyMiniCaptcha(@RequestBody @Valid CaptchaVerifyVo vo, @PathVariable String appId)
            throws Exception {
        smsServiceImpl.verifyCaptcha(vo);
        return "ok";
    }

}
