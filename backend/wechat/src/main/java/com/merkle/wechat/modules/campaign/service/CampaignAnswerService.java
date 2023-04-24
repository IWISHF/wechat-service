package com.merkle.wechat.modules.campaign.service;

import java.util.Map;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.CampaignAnswer;
import com.merkle.wechat.common.entity.campaign.CampaignOfflineCheckInLog;

public interface CampaignAnswerService {

    void createCampaignAnswer(CampaignAnswer answer, WechatPublicNo pbNo) throws Exception;

    boolean alreadyAnswerd(Long campaignId, String openid, Long channelId) throws Exception;

    CampaignAnswer getAnswer(Long campaignId, String openid, Long channelId);

    void createOfflineLog(CampaignOfflineCheckInLog log) throws Exception;

    boolean alreadyCheckInOfflineCampaign(Long campaignId, String openid, Long channelId) throws Exception;

    Map<String, Object> checkCampaignStatusAndStock(Long campaignId, String openid, Long chanelId) throws Exception;

    Map<String, Object> checkCampaignInventory(Long campaignId, String openid, Long chanelId) throws Exception;

    Map<String, Object> checkRewardInventory(Long campaignId, Long chanelId) throws Exception;
}
