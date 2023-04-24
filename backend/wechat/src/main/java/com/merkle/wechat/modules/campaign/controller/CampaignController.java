package com.merkle.wechat.modules.campaign.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.PreferLanguage;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.Campaign;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.campaign.service.CampaignService;
import com.merkle.wechat.modules.campaign.service.PreferLanguageServiceImpl;
import com.merkle.wechat.service.WechatPublicNoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "活动管理")
@RestController
@RequestMapping("/wechat/{channelId}/campaign")
public class CampaignController extends AbstractController {
    private @Autowired CampaignService campaignServiceImpl;
    private @Autowired WechatPublicNoService publicNoServiceImpl;
    private @Autowired PreferLanguageServiceImpl preferLanguageServiceImpl;

    @NeedWrap
    @GetMapping("/progress")
    @ApiOperation("获取活动")
    public List<Campaign> getInProgressCampaigns(@PathVariable Long channelId,
            @RequestParam(defaultValue = "false") @ApiParam(defaultValue = "false", required = false) boolean isOffline)
            throws Exception {
        if (isOffline) {
            return campaignServiceImpl.findOfflineProgressCampaigns(channelId);
        }
        return campaignServiceImpl.findInProgressCampaigns(channelId);
    }

    @NeedWrap
    @GetMapping("/progress/hot")
    @ApiOperation("获取活动")
    public List<Campaign> getInProgressHotCampaigns(@PathVariable Long channelId) throws Exception {
        return campaignServiceImpl.findInProgressHotCampaigns(channelId);
    }

    @NeedWrap
    @GetMapping("/answered/{openid}")
    @ApiOperation("获取参与过的活动")
    public List<Campaign> getAnsweredCampaigns(@PathVariable String openid, @PathVariable Long channelId)
            throws Exception {
        return campaignServiceImpl.getAnsweredCampaigns(channelId, openid);
    }

    @NeedWrap
    @GetMapping("/{id}")
    @ApiOperation("获取活动")
    public Campaign getCampaign(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        return campaignServiceImpl.findCampaignByWechatPublicNoIdAndId(channelId, id);
    }

    @NeedWrap
    @PostMapping
    @ApiOperation("创建活动")
    public String createCampaign(@RequestBody Campaign campaign, @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = publicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        campaignServiceImpl.createCampaign(campaign, pbNo);
        return "ok";
    }

    @NeedWrap
    @PostMapping("/prefer/lang")
    @ApiOperation("创建或者修改活动倾向语言")
    public String preferLanguage(@RequestBody @Valid PreferLanguage preferLanguage, @PathVariable Long channelId)
            throws Exception {
        WechatPublicNo pbNo = publicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        preferLanguageServiceImpl.saveOrUpdate(preferLanguage, pbNo);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/prefer/lang/{openid}")
    @ApiOperation("获取活动倾向语言")
    public PreferLanguage getPreferLanguage(@PathVariable Long channelId, @PathVariable String openid)
            throws Exception {
        WechatPublicNo pbNo = publicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return preferLanguageServiceImpl.getLanguage(pbNo, openid);

    }

}
