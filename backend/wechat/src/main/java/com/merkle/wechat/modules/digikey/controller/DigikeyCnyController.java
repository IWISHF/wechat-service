package com.merkle.wechat.modules.digikey.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyCnyServiceImpl;
import com.merkle.wechat.modules.digikey.vo.CnyItemOpenVo;
import com.merkle.wechat.modules.digikey.vo.CnyItemVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/wechat/digikey/cny")
@Api(tags = "DIGIKEY CNY API")
public class DigikeyCnyController extends AbstractController {
    private @Autowired DigikeyCnyServiceImpl cnyServiceImpl;
    private @Autowired FollowerDao followerDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    @NeedWrap
    @GetMapping("/items")
    @ApiOperation("获取红包列表")
    public List<CnyItemVo> getAllItems(@RequestParam @Valid @NotEmpty String openid) throws Exception {
        return cnyServiceImpl.getCnyItemVosByOpenId(openid);
    }

    @NeedWrap
    @PostMapping("/open")
    @ApiOperation("打开红包")
    public CnyItemVo lightUp(@RequestBody @Valid CnyItemOpenVo vo) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(vo.getOpenid(), digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return cnyServiceImpl.openCnyItem(vo);
    }

    @NeedWrap
    @GetMapping("/view/tools")
    @ApiOperation("访问官网加分")
    public String viewTools(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty String toolId) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        cnyServiceImpl.viewTool(openid, toolId);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/share/tools")
    @ApiOperation("分享DK tools")
    public String shareLog(@RequestParam(required = true) @Valid @NotEmpty String openid,
            @RequestParam(required = true) @Valid @NotEmpty String content) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        cnyServiceImpl.shareToolLimit3(openid, content);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/watch/video")
    @ApiOperation("看视频")
    public boolean watchVideo(@RequestParam(required = true) @Valid @NotEmpty String openid) throws Exception {
        Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, digikeyPubAppId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        return cnyServiceImpl.watchVideo(openid);
    }
}
