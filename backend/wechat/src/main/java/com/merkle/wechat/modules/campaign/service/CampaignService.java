package com.merkle.wechat.modules.campaign.service;

import java.util.List;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.Campaign;

public interface CampaignService {

    Campaign findCampaignByWechatPublicNoIdAndId(Long wechatPublicNoId, Long id);

    void createCampaign(Campaign campaign, WechatPublicNo pbNo);

    List<Campaign> findInProgressCampaigns(Long channelId) throws Exception;

    List<Campaign> findInProgressHotCampaigns(Long channelId) throws Exception;

    List<Campaign> getAnsweredCampaigns(Long channelId, String openid);

    List<Campaign> findOfflineProgressCampaigns(Long channelId);

}
