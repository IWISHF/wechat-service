package com.merkle.wechat.modules.tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.tracking.TrackCampaignPageEventDao;
import com.merkle.wechat.common.entity.tracking.TrackCampaignPageEvent;

@Component
public class TrackCampaignPageEventServiceImpl implements TrackCampaignPageEventService {
    private @Autowired TrackCampaignPageEventDao trackCPEDaoImpl;

    @Override
    public TrackCampaignPageEvent saveCPE(TrackCampaignPageEvent event) {
        return trackCPEDaoImpl.save(event);
    }

}
