package com.merkle.wechat.common.dao.campaign;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.campaign.CampaignAnswer;

@Repository
public interface CampaignAnswerDao extends PagingAndSortingRepository<CampaignAnswer, Long> {

    CampaignAnswer findByOpenid(String openid);

    boolean existsByOpenid(String openid);

    boolean existsByOpenidAndCampaignIdAndWechatPublicNoId(String openid, Long campaignId, Long id);

    CampaignAnswer findFirstByOpenidAndCampaignIdAndWechatPublicNoId(String openid, Long campaignId, Long channelId);

    List<CampaignAnswer> findByOpenidAndWechatPublicNoId(String openid, Long channelId);

    List<CampaignAnswer> findByCampaignId(Long id);

    boolean existsByUnionidAndCampaignId(String openid, Long campaignId);

    CampaignAnswer findFirstByUnionidAndCampaignId(String unionid, Long campaignId);

    List<CampaignAnswer> findByUnionid(String unionid);

}
