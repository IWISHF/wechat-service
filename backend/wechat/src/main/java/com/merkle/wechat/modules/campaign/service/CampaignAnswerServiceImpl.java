package com.merkle.wechat.modules.campaign.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.loyalty.response.PrismResponse;
import com.merkle.wechat.common.dao.RewardsRedeemLogDao;
import com.merkle.wechat.common.dao.campaign.CampaignAnswerDao;
import com.merkle.wechat.common.dao.campaign.CampaignDao;
import com.merkle.wechat.common.dao.campaign.CampaignOfflineCheckInLogDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.Campaign;
import com.merkle.wechat.common.entity.campaign.CampaignAnswer;
import com.merkle.wechat.common.entity.campaign.CampaignOfflineCheckInLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.digikey.service.TemplateMessageServiceImpl;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.modules.survey.service.SurveyAnswerService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.util.DatesUtil;

@Component
public class CampaignAnswerServiceImpl implements CampaignAnswerService {
    private @Autowired CampaignAnswerDao campaignAnswerDaoImpl;
    private @Autowired RewardsRedeemLogDao rewardsRedeemLogDaoImpl;
    private @Autowired CampaignOfflineCheckInLogDao campaignOfflineCheckInLogDaoImpl;
    private @Autowired CampaignDao campaignDaoImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired TemplateMessageServiceImpl digikeyTemplateMessageServiceImpl;
    private @Autowired FollowerService followerServiceImpl;
    private @Autowired SurveyAnswerService surveyAnswerService;

    // 未了让前台快速完成 写的save所有的options
    @Override
    public void createCampaignAnswer(CampaignAnswer answer, WechatPublicNo pbNo) throws Exception {
        Campaign c = campaignDaoImpl.findOne(answer.getCampaignId());
        Date currentDate = new Date();

        if ((c.getEndDate() != null && currentDate.after(c.getEndDate())) || currentDate.before(c.getStartDate())
                || !c.isEnable()) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }

        Follower f = followerServiceImpl.findOneByOpenid(answer.getOpenid());

        if (!c.isMulti()) {
            boolean exist = alreadyAnswerd(answer.getCampaignId(), f, pbNo.getId());
            if (exist) {
                throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
            }
        }

        answer.setUnionid(f.getUnionid());
        answer.setWechatPublicNoId(pbNo.getId());
        answer.setCreatedDate(new Date());
        answer.getQuestions().forEach((question) -> {
            question.getOptions().forEach((option) -> {
                option.setCreatedDate(new Date());
            });
            question.setCreatedDate(new Date());
        });
        campaignAnswerDaoImpl.save(answer);
    }

    @Override
    public boolean alreadyAnswerd(Long campaignId, String openid, Long channelId) throws Exception {
        Follower f = followerServiceImpl.findOneByOpenid(openid);
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            return campaignAnswerDaoImpl.existsByUnionidAndCampaignId(f.getUnionid(), campaignId);
        }
        return campaignAnswerDaoImpl.existsByOpenidAndCampaignIdAndWechatPublicNoId(openid, campaignId, channelId);
    }

    private boolean alreadyAnswerd(Long campaignId, Follower f, Long channelId) throws Exception {
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            return campaignAnswerDaoImpl.existsByUnionidAndCampaignId(f.getUnionid(), campaignId);
        }
        return campaignAnswerDaoImpl.existsByOpenidAndCampaignIdAndWechatPublicNoId(f.getOpenid(), campaignId,
                channelId);
    }

    @Override
    public boolean alreadyCheckInOfflineCampaign(Long campaignId, String openid, Long channelId) throws Exception {
        Follower f = followerServiceImpl.findOneByOpenid(openid);
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            return campaignOfflineCheckInLogDaoImpl.existsByUnionidAndCampaignId(f.getUnionid(), campaignId);
        }
        return campaignOfflineCheckInLogDaoImpl.existsByOpenidAndCampaignIdAndWechatPublicNoId(openid, campaignId,
                channelId);
    }

    private boolean alreadyCheckInOfflineCampaign(Long campaignId, Follower f, Long channelId) {
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            return campaignOfflineCheckInLogDaoImpl.existsByUnionidAndCampaignId(f.getUnionid(), campaignId);
        }
        return campaignOfflineCheckInLogDaoImpl.existsByOpenidAndCampaignIdAndWechatPublicNoId(f.getOpenid(),
                campaignId, channelId);
    }

    @Override
    public CampaignAnswer getAnswer(Long campaignId, String openid, Long channelId) {
        Follower f = followerServiceImpl.findOneByOpenid(openid);
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            return campaignAnswerDaoImpl.findFirstByUnionidAndCampaignId(f.getUnionid(), campaignId);
        }
        return campaignAnswerDaoImpl.findFirstByOpenidAndCampaignIdAndWechatPublicNoId(openid, campaignId, channelId);
    }

    @Override
    public void createOfflineLog(CampaignOfflineCheckInLog log) throws Exception {
        Follower follower = followerServiceImpl.findOneByOpenid(log.getOpenid());
        log.setUnionid(follower.getUnionid());
        boolean exist = alreadyCheckInOfflineCampaign(log.getCampaignId(), follower, log.getWechatPublicNoId());
        if (exist) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }
        Campaign campaign = campaignDaoImpl.findOne(log.getCampaignId());
        if (campaign.isRecordOfflineCheckinEvent()) {
            PrismResponse response = loyaltyServiceImpl.recordOfflineCheckinEvent(log.getOpenid(),
                    log.getWechatPublicNoId());
            if (response.toPrismData().isSuccess()) {
                log.setLoyaltyOfflineEventSyncStatus(true);
                log = digikeyTemplateMessageServiceImpl.sendOfflineCheckInSuccessMessage(follower, log);
                campaignOfflineCheckInLogDaoImpl.save(log);
            } else {
                throw new ServiceWarn(ExceptionConstants.CAMPAIGN_OFFLINE_CHECKIN_ERROR);
            }
        } else {
            log.setTemplateErrorMessage("no need send event!");
            campaignOfflineCheckInLogDaoImpl.save(log);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> checkCampaignStatusAndStock(Long campaignId, String openid, Long chanelId) throws Exception {
        Map<String, Object> res = new HashMap<>();
        String earnedProduct = null;
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        Campaign campaign = campaignDaoImpl.findOne(campaignId);
        JsonJsonParser jsonParser = new JsonJsonParser();
        Map<String, Object> rewardConfigs = jsonParser.parseMap(campaign.getDescriptionHtml());
        List<Object> rewards = (ArrayList<Object>)rewardConfigs.get("configs");
        CampaignOfflineCheckInLog checkLog = campaignOfflineCheckInLogDaoImpl.findOneByCampaignIdAndUnionidAndWechatPublicNoId(campaignId, follower.getUnionid(), chanelId);
        rewards.forEach(reward -> {
            Map<String, Object> config = (LinkedHashMap<String, Object>)reward;
            int redeemCnt = campaignOfflineCheckInLogDaoImpl.countByCampaignIdAndCampaignTitleAndCreatedDateBetween(
                    campaignId,
                    config.get("name").toString(),
                    DatesUtil.getCurrentDayStartTime(),
                    DatesUtil.getCurrentDayEndTime());
            // Hard code for 2021 DK electronic campaign
            if ("backpack".equals(config.get("name").toString())) {
                redeemCnt = surveyAnswerService.countByIdAndCreatedDateBetween(66L, DatesUtil.getCurrentDayStartTime(), DatesUtil.getCurrentDayEndTime());
            }
            config.put("status", (int)config.get("qty") - redeemCnt < (int)config.get("alert"));
            config.put("left_inventory", (int)config.get("qty") - redeemCnt);
            config.put("redeemed_cnt", redeemCnt);
        });
        if (checkLog != null) {
            earnedProduct = checkLog.getCampaignTitle();
        }
        res.put("earned_product", earnedProduct);
        res.put("rewards", rewards);
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> checkRewardInventory(Long campaignId, Long chanelId) throws Exception {
        Map<String, Object> res = new HashMap<>();

        Campaign campaign = campaignDaoImpl.findOne(campaignId);
        if (campaign == null) {
            throw new ServiceWarn(ExceptionConstants.MISSING_PARAM_ERROR);
        }
        JsonJsonParser jsonParser = new JsonJsonParser();
        Map<String, Object> rewardConfigs = jsonParser.parseMap(campaign.getDescriptionHtml());
        List<Object> rewards = (ArrayList<Object>)rewardConfigs.get("configs");
        rewards.forEach(reward -> {
            Map<String, Object> config = (LinkedHashMap<String, Object>)reward;
            int redeemCnt = rewardsRedeemLogDaoImpl.countByRewardIdAndRedeemStatus(config.get("rewardId").toString(), true);
            String status = "enough";
            int left = (int)config.get("qty") - redeemCnt;
            if (left <= 0) {
                status = "none";
            } else if (left < (int)config.get("alert")) {
                status = "short";
            }
            config.put("status", status);
        });
        res.put("rewards", rewards);
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> checkCampaignInventory(Long campaignId, String openid, Long chanelId) throws Exception {
        Map<String, Object> res = new HashMap<>();
        String earnedProduct = null;
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        if (follower == null) {
            throw new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST);
        }

        Campaign campaign = campaignDaoImpl.findOne(campaignId);
        JsonJsonParser jsonParser = new JsonJsonParser();
        Map<String, Object> rewardConfigs = jsonParser.parseMap(campaign.getDescriptionHtml());
        List<Object> rewards = (ArrayList<Object>)rewardConfigs.get("configs");
        CampaignOfflineCheckInLog checkLog = campaignOfflineCheckInLogDaoImpl.findOneByCampaignIdAndUnionidAndWechatPublicNoId(campaignId, follower.getUnionid(), chanelId);
        rewards.forEach(reward -> {
            Map<String, Object> config = (LinkedHashMap<String, Object>)reward;
            int redeemCnt = campaignOfflineCheckInLogDaoImpl.countByCampaignIdAndCampaignTitleAndCreatedDateBetween(
                    campaignId,
                    config.get("name").toString(),
                    DatesUtil.getCurrentDayStartTime(),
                    DatesUtil.getCurrentDayEndTime());
            config.put("status", (int)config.get("qty") - redeemCnt < (int)config.get("alert"));
        });
        if (checkLog != null) {
            earnedProduct = checkLog.getCampaignTitle();
        }
        res.put("earned_product", earnedProduct);
        res.put("rewards", rewards);
        return res;
    }
}
