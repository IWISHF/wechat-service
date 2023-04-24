package com.merkle.wechat.modules.campaign.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.dao.campaign.CampaignDao;
import com.merkle.wechat.common.dao.campaign.CampaignOfflineCheckInLogDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.Campaign;
import com.merkle.wechat.common.entity.campaign.CampaignAnswer;
import com.merkle.wechat.common.entity.campaign.CampaignOfflineCheckInLog;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.campaign.service.CampaignAnswerService;
import com.merkle.wechat.service.WechatPublicNoServiceImpl;
import com.merkle.wechat.util.DatesUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "活动参与API")
@RequestMapping("/wechat/{channelId}/campaign/answer")
public class CampaignAnswerController extends AbstractController {
    private @Autowired CampaignAnswerService campaignAnswerServiceImpl;
    private @Autowired WechatPublicNoServiceImpl publicNoServiceImpl;
    private @Autowired CampaignOfflineCheckInLogDao campaignOfflineCheckInLogDaoImpl;
    private @Autowired CampaignDao campaignDaoImpl;
    // private @Autowired TagService tagServiceImpl;

    @NeedWrap
    @PostMapping
    @ApiOperation("参与活动")
    public String createCampaignAnswer(@RequestBody CampaignAnswer answer, @PathVariable Long channelId)
            throws Exception {
        WechatPublicNo pbNo = publicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        campaignAnswerServiceImpl.createCampaignAnswer(answer, pbNo);
        return "ok";
    }

    @SuppressWarnings("unchecked")
    @NeedWrap
    @PostMapping("/offline")
    @ApiOperation("参与线下活动")
    public String offlineCheckin(@RequestBody @Valid CampaignOfflineCheckInLog log) throws Exception {
        if (log.getCampaignId().equals(21L)) {
            Campaign campaign = campaignDaoImpl.findOne(log.getCampaignId());
            JsonJsonParser jsonParser = new JsonJsonParser();
            Map<String, Object> rewardConfigs = jsonParser.parseMap(campaign.getDescriptionHtml());
            List<Object> rewards = (ArrayList<Object>)rewardConfigs.get("configs");
            rewards.forEach(reward -> {
                Map<String, Object> config = (LinkedHashMap<String, Object>)reward;
                int redeemCnt = campaignOfflineCheckInLogDaoImpl.countByCampaignIdAndCampaignTitleAndCreatedDateBetween(
                        log.getCampaignId(),
                            config.get("name").toString(),
                            DatesUtil.getCurrentDayStartTime(),
                            DatesUtil.getCurrentDayEndTime());
                config.put("status", (int)config.get("qty") - redeemCnt < (int)config.get("alert"));
                if (log.getCampaignTitle().equals(config.get("name").toString()) &&
                    (int)config.get("qty") - redeemCnt < (int)config.get("alert")) {
                    throw new ServiceWarn(ExceptionConstants.REACH_LIMITATION);
                }
            });
        }
        campaignAnswerServiceImpl.createOfflineLog(log);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/offline/{campaignId}/check")
    @ApiOperation("是否参与线下活动")
    public boolean alreadyCheckInOffline(@PathVariable Long campaignId, @RequestParam(required = true) String openid,
            @PathVariable Long channelId) throws Exception {

        return campaignAnswerServiceImpl.alreadyCheckInOfflineCampaign(campaignId, openid, channelId);
    }

    @NeedWrap
    @GetMapping("/{campaignId}/check")
    @ApiOperation("是否参与活动")
    public CampaignAnswer alreadyAnswered(@PathVariable Long campaignId, @RequestParam(required = true) String openid,
            @PathVariable Long channelId) throws Exception {
        return campaignAnswerServiceImpl.getAnswer(campaignId, openid, channelId);
    }
}
