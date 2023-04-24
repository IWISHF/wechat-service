package com.merkle.wechat.common.dao.campaign;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.campaign.CampaignOfflineCheckInLog;

@Repository
public interface CampaignOfflineCheckInLogDao extends PagingAndSortingRepository<CampaignOfflineCheckInLog, Long> {

    boolean existsByOpenidAndCampaignIdAndWechatPublicNoId(String openid, Long campaignId, Long channelId);

    List<CampaignOfflineCheckInLog> findByOpenidAndWechatPublicNoId(String openid, Long channelId);

    boolean existsByUnionidAndCampaignId(String unionid, Long campaignId);

    List<CampaignOfflineCheckInLog> findByUnionid(String unionid);

    int countByCampaignIdAndCampaignTitleAndCreatedDateBetween(Long campaignId, String campaignTitle, Date startOfDay, Date endOfDay);

    CampaignOfflineCheckInLog findOneByCampaignIdAndUnionidAndWechatPublicNoId(Long campaignId, String Unionid, Long wechatPublicNoId);
}
