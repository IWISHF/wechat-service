package com.merkle.wechat.modules.digikey.controller;

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
import com.merkle.wechat.common.entity.digikey.DigikeyCalenderCampaign;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyCalenderCampaignServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "DigikeyCalenderCampaignAPI")
@Controller
@RequestMapping("/wechat/digikey")
public class DigikeyCalenderCampaignController extends AbstractController {
    private @Autowired DigikeyCalenderCampaignServiceImpl serviceImpl;

    @NeedWrap
    @ApiOperation("create")
    @PostMapping("/campaign/calender")
    public String save(@RequestBody @Valid DigikeyCalenderCampaign campaign) throws Exception {
        serviceImpl.save(campaign);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("check")
    @GetMapping("/campaign/calender/check/{openid}")
    public boolean check(@PathVariable @Valid @NotEmpty String openid) {
        return serviceImpl.check(openid);
    }

    @NeedWrap
    @ApiOperation("get")
    @GetMapping("/campaign/calender/{openid}")
    public DigikeyCalenderCampaign get(@PathVariable @Valid @NotEmpty String openid) throws Exception {
        return serviceImpl.getByOpenid(openid);
    }

}
