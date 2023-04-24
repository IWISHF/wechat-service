package com.merkle.wechat.modules.tracking.service;

import com.merkle.wechat.common.entity.tracking.TrackCampaignPageEvent;

public interface TrackCampaignPageEventService {

    TrackCampaignPageEvent saveCPE(TrackCampaignPageEvent event);

}
