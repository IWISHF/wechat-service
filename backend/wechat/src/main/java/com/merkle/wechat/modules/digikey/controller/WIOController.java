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
import com.merkle.wechat.modules.digikey.service.DigikeyVideoServiceImpl;
import com.merkle.wechat.modules.digikey.service.WIOServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/wechat/digikey/wio")
@Api(tags = "Digikey WIO APIs")
public class WIOController extends AbstractController {
    private @Autowired WIOServiceImpl wioServiceImpl;
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired DigikeyVideoServiceImpl digikeyVideoServiceImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    @NeedWrap
    @GetMapping("/share")
    @ApiOperation("分享活动页面，获得积分")
    public boolean share(
            @RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("请传Video Id") String content,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("事件详情") String detail) {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return wioServiceImpl.shareLimitWeek(openid, content, detail);
    }


    @NeedWrap
    @GetMapping("/watch/video")
    @ApiOperation("看视频")
    public boolean watchVideo(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("video id must exist") long id,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("事件详情") String detail) throws Exception {
        Optional.ofNullable(digikeyVideoServiceImpl.findById(id))
        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return wioServiceImpl.watchVideo(openid, id, detail);
    }
}
