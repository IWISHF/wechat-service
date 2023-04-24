package com.merkle.wechat.modules.digikey.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyFrameArduinoServiceImpl;
import com.merkle.wechat.modules.digikey.service.DigikeyVideoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/wechat/digikey/frame/arduino")
@Api(tags = "DIGIKEY FRAME ARDUINO API")
public class DigikeyFrameArduinoController extends AbstractController {
    private @Autowired DigikeyFrameArduinoServiceImpl arduinoServiceImpl;
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired DigikeyVideoServiceImpl digikeyVideoServiceImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    @GetMapping("/share")
    @ApiOperation("分享活动")
    public boolean shareLog(@RequestParam(required = true) @Valid @NotEmpty String openid) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return arduinoServiceImpl.shareLimit3(openid, "digikey_frame_arduino");
    }

    @NeedWrap
    @GetMapping("/watch/video")
    @ApiOperation("看视频")
    public boolean watchVideo(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("video id must exist") long id) throws Exception {
        Optional.ofNullable(digikeyVideoServiceImpl.findById(id))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return arduinoServiceImpl.watchVideo(openid, id);
    }
}
