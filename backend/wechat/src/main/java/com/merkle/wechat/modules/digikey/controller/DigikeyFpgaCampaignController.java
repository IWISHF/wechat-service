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
import com.merkle.wechat.modules.digikey.service.DigikeyFpgaServiceImpl;
import com.merkle.wechat.modules.digikey.service.DigikeyVideoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/wechat/digikey/fpga")
@Api(value = "DigiKey FPGA Campaign", tags = "得捷电子 Seeed FPGA 大学堂活动")
public class DigikeyFpgaCampaignController extends AbstractController {
    private @Autowired DigikeyFpgaServiceImpl fpgaService;
    private @Autowired DigikeyVideoServiceImpl digikeyVideoServiceImpl;
    private @Autowired FollowerDao followerDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    @NeedWrap
    @GetMapping("/watch/video")
    @ApiOperation("看视频，加积分")
    public boolean watchVideo(
            @RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("video id must exist") long vid) throws Exception {
        Optional.ofNullable(digikeyVideoServiceImpl.findById(vid))
        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return fpgaService.watchVideo(openid, vid);
    }

    @NeedWrap
    @GetMapping("/share")
    @ApiOperation("分享活动页面，获得积分")
    public boolean share(
            @RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty @ApiParam("请传Video Id") String content) {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return fpgaService.shareLimitWeek(openid, content);
    }

    @NeedWrap
    @GetMapping("/check/lucky/draw")
    @ApiOperation("检查用户是否完成了6个视频的观看，如果完成了可以参加lucky draw的form表单")
    public boolean checkLuckyDraw(@RequestParam(required = true) @Valid @NotEmpty String openid) {
        return fpgaService.checkLuckyDraw(openid);
    }
}
