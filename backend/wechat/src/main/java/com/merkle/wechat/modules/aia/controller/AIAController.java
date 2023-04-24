package com.merkle.wechat.modules.aia.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.aia.AIAIntentionData;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.aia.service.AIAIntentionDataService;
import com.merkle.wechat.modules.aia.service.CellMessageService;
import com.merkle.wechat.modules.aia.vo.AIAUserInfoVo;
import com.merkle.wechat.modules.aia.vo.GetUserInfoVo;
import com.merkle.wechat.modules.aia.vo.SendCaptchaVo;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.follower.FollowerService;
import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "AIA 接口")
@RequestMapping("/wechat/api/aia")
public class AIAController extends AbstractController {
    private @Autowired AIAIntentionDataService aiaIntentionDataServiceImpl;
    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;
    private @Autowired CellMessageService cellMessageServiceImpl;
    private @Autowired FollowerService followerServiceImpl;

    @NeedWrap
    @ApiOperation("意向收集")
    @PostMapping("/intention/info")
    public String intentionInfoCreate(@RequestBody @Valid AIAIntentionData data) throws Exception {
        aiaIntentionDataServiceImpl.createIntenionData(data);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("发送手机验证码")
    @PostMapping("/captcha/send")
    public String sendCaptcha(@RequestBody @Valid SendCaptchaVo vo) throws Exception {
        wechatPbNoServiceImpl.findByIdOrThrowNotExistException(vo.getChannelId());
        cellMessageServiceImpl.sendCaptcha(vo.getPhone(), vo.getOpenid(), vo.getChannelId());
        return "ok";
    }

    @NeedWrap
    @ApiOperation("创建用户信息")
    @PostMapping("/user/info")
    public String createUserInfo(@RequestBody @Valid AIAUserInfoVo vo) throws Exception {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findByIdOrThrowNotExistException(vo.getChannelId());
        cellMessageServiceImpl.verifyCaptchaAndCreateAIAUserInfo(vo, pbNo);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("获取用户信息")
    @GetMapping("/user/info")
    public GetUserInfoVo getAIAUserInfo(@NotEmpty @Valid String openid, @NotNull @Valid Long channelId)
            throws Exception {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return cellMessageServiceImpl.getAIAUserInfo(openid, pbNo);
    }

    @NeedWrap
    @ApiOperation("检验粉丝是否注册")
    @GetMapping("/follower/check/{openid}")
    public boolean check(@PathVariable String openid) throws Exception {
        Optional<Follower> follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid));
        if (follower.isPresent() && follower.get().getSubscribe().intValue() != 0) {
            return true;
        }
        return false;
    }

}
