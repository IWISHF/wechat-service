package com.merkle.wechat.common.dao.tracking;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.tracking.TrackCampaignPageEvent;

@Repository
public interface TrackCampaignPageEventDao extends PagingAndSortingRepository<TrackCampaignPageEvent, Long> {

}
