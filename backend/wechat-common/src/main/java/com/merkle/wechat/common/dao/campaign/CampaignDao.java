package com.merkle.wechat.common.dao.campaign;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.campaign.Campaign;

@Repository
public interface CampaignDao extends PagingAndSortingRepository<Campaign, Long> {

    Campaign findByWechatPublicNoIdAndId(Long wechatPublicNoId, Long id);

    @Query(nativeQuery = true, value = "select * from campaign where wechatPublicNoId = :wechatPublicNoId and enable = true and startDate <= :currentDate and (endDate >= :currentDate or endDate is null) order by startDate DESC")
    List<Campaign> findInProgressCampaign(@Param("wechatPublicNoId") Long wechatPublicNoId,
            @Param("currentDate") Date currentDate);

    @Query(nativeQuery = true, value = "select * from campaign where wechatPublicNoId = :wechatPublicNoId and enable = true and hot = true and startDate <= :currentDate and (endDate >= :currentDate or endDate is null) order by startDate DESC")
    List<Campaign> findInProgressHotCampaign(@Param("wechatPublicNoId") Long wechatPublicNoId,
            @Param("currentDate") Date currentDate);

    @Query(nativeQuery = true, value = "select * from campaign where wechatPublicNoId = :wechatPublicNoId and enable = true and offline = true and startDate <= :currentDate and (endDate >= :currentDate or endDate is null) order by startDate DESC")
    List<Campaign> findOfflineInProgressCampaign(@Param("wechatPublicNoId") Long wechatPublicNoId,
            @Param("currentDate") Date currentDate);

}
