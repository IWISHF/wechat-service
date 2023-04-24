package com.merkle.wechat.modules.digikey.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.WishServiceImpl;
import com.merkle.wechat.modules.digikey.vo.LightUpVo;
import com.merkle.wechat.modules.digikey.vo.WishItemVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/wechat/digikey/wish")
@Api(tags = "DIGIKEY WISH API")
public class DigikeyWishController extends AbstractController {
    private @Autowired WishServiceImpl wishServiceImpl;
    private @Autowired FollowerDao followerDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    @NeedWrap
    @PostMapping("/lightup")
    @ApiOperation("点亮产品")
    public WishItemVo lightUp(@RequestBody @Valid LightUpVo vo) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(vo.getOpenid(), digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return wishServiceImpl.lightUp(vo);
    }

    @NeedWrap
    @PostMapping("/v2/lightup")
    @ApiOperation("点亮产品")
    public WishItemVo v2LightUp(@RequestBody @Valid LightUpVo vo) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(vo.getOpenid(), digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return wishServiceImpl.lightUpV2(vo);
    }

    @NeedWrap
    @GetMapping("/items")
    @ApiOperation("获取产品列表")
    public List<WishItemVo> getAllItems(@RequestParam @Valid @NotEmpty String openid) throws Exception {
        return wishServiceImpl.getWishItemVosByOpenId(openid);
    }

    @NeedWrap
    @GetMapping("/perfect/trigger")
    @ApiOperation("激活所有完美参与用户")
    public String trigger(@RequestParam @Valid @NotEmpty String start) throws Exception {
        if (start.equals("maimaipio123_")) {
            AsyncUtil.asyncRun(() -> {
                wishServiceImpl.triggerPerfect();
            });
        }
        return "ok";
    }

    @NeedWrap
    @GetMapping("/share/campaign")
    @ApiOperation("分享活动")
    public String shareLog(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty String content) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        wishServiceImpl.shareCampaignLimit2(openid, content);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/v2/share/campaign")
    @ApiOperation("分享活动")
    public String v2ShareLog(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty String content) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        wishServiceImpl.shareCampaignLimit(openid, content, 3);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/view/product")
    @ApiOperation("跳转官网加分")
    public String viewProduct(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty String productid) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        wishServiceImpl.viewProduct(openid, productid);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/v2/view/product")
    @ApiOperation("跳转官网加分")
    public String v2ViewProduct(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty String productid) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        wishServiceImpl.viewProductV2(openid, productid);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/items/{campaignId}")
    @ApiOperation("根据campaign获取产品列表")
    public List<WishItemVo> getItemsByCampaign(@RequestParam @Valid @NotEmpty String openid,
            @PathVariable Long campaignId) throws Exception {
        return wishServiceImpl.getWishItemVosByOpenIdAndCampaign(openid, campaignId);
    }
}
