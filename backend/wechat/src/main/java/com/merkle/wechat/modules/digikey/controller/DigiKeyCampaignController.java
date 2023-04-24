package com.merkle.wechat.modules.digikey.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.campaign.service.CampaignAnswerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "DigiKey 独有的活动API")
@RequestMapping("/wechat/digikey/{channelId}/campaign")
public class DigiKeyCampaignController extends AbstractController {
    private @Autowired CampaignAnswerService campaignAnswerServiceImpl;

    @NeedWrap
    @GetMapping("/{campaignId}/check/stock")
    @ApiOperation("是否参与线下活动,并返回礼品对应的库存")
    public Map<String, Object> checkOfflineStatus(
            @PathVariable Long campaignId,
            @RequestParam(required = true) String openid,
            @PathVariable Long channelId) throws Exception {
        return campaignAnswerServiceImpl.checkCampaignStatusAndStock(campaignId, openid, channelId);
    }

    @NeedWrap
    @GetMapping("/{campaignId}/reward/inventory")
    @ApiOperation("返回礼品对应的库存")
    public Map<String, Object> checkRewardInventory(
            @PathVariable Long campaignId,
            @PathVariable Long channelId) throws Exception {
        return campaignAnswerServiceImpl.checkRewardInventory(campaignId, channelId);
    }
}
