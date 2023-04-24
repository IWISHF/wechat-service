package com.merkle.wechat.modules.campaign.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.ChannelBindConfigDao;
import com.merkle.wechat.common.dao.campaign.CampaignAnswerDao;
import com.merkle.wechat.common.dao.campaign.CampaignDao;
import com.merkle.wechat.common.dao.campaign.CampaignOfflineCheckInLogDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.ChannelBindConfig;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.campaign.Campaign;
import com.merkle.wechat.common.entity.campaign.CampaignAnswer;
import com.merkle.wechat.common.entity.campaign.CampaignOfflineCheckInLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.modules.digikey.service.DigikeyCalenderCampaignServiceImpl;

@Component
public class CampaignServiceImpl implements CampaignService {
    private @Autowired CampaignDao campaignDaoImpl;
    private @Autowired CampaignAnswerDao campaignAnswerDaoImpl;
    private @Autowired DigikeyCalenderCampaignServiceImpl digikeyCalenderCampaignServiceImpl;
    private @Autowired CampaignOfflineCheckInLogDao campaignOfflineCheckInLogDaoImpl;
    private @Autowired ChannelBindConfigDao channelBindConfigDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;

    @Override
    public Campaign findCampaignByWechatPublicNoIdAndId(Long wechatPublicNoId, Long id) {
        Campaign c = campaignDaoImpl.findOne(id);
        if (c == null) {
            return null;
        }
        if (c.getWechatPublicNoId() == wechatPublicNoId) {
            return c;
        }
        List<ChannelBindConfig> configs = channelBindConfigDaoImpl.findByFrom(wechatPublicNoId);
        if (configs != null && configs.size() > 0) {
            for (ChannelBindConfig config : configs) {
                if (c.getWechatPublicNoId() == config.getTo()) {
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public void createCampaign(Campaign campaign, WechatPublicNo pbNo) {
        campaign.setWechatPublicNoId(pbNo.getId());
        campaignDaoImpl.save(campaign);
    }

    @Override
    public List<Campaign> findInProgressCampaigns(Long channelId) throws Exception {
        Date currentDate = new Date();
        List<Campaign> total = new ArrayList<>();
        List<Campaign> campaigns = campaignDaoImpl.findInProgressCampaign(channelId, currentDate);
        if (campaigns != null && campaigns.size() > 0) {
            total.addAll(campaigns);
        }
        List<ChannelBindConfig> configs = channelBindConfigDaoImpl.findByFrom(channelId);
        if (configs != null && configs.size() > 0) {
            for (ChannelBindConfig config : configs) {
                List<Campaign> temp = campaignDaoImpl.findInProgressCampaign(config.getTo(), currentDate);
                if (temp != null && temp.size() > 0) {
                    total.addAll(temp);
                }
            }
        }
        return total;
    }

    @Override
    public List<Campaign> findInProgressHotCampaigns(Long channelId) throws Exception {
        Date currentDate = new Date();
        List<Campaign> total = new ArrayList<>();
        List<Campaign> campaigns = campaignDaoImpl.findInProgressHotCampaign(channelId, currentDate);
        if (campaigns != null && campaigns.size() > 0) {
            total.addAll(campaigns);
        }
        List<ChannelBindConfig> configs = channelBindConfigDaoImpl.findByFrom(channelId);
        if (configs != null && configs.size() > 0) {
            for (ChannelBindConfig config : configs) {
                List<Campaign> temp = campaignDaoImpl.findInProgressHotCampaign(config.getTo(), currentDate);
                if (temp != null && temp.size() > 0) {
                    total.addAll(temp);
                }
            }
        }
        return total;
    }

    @Override
    public List<Campaign> getAnsweredCampaigns(Long channelId, String openid) {
        List<Long> ids = new ArrayList<>();
        Follower f = followerDaoImpl.findOneByOpenid(openid);
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            List<CampaignAnswer> answers = campaignAnswerDaoImpl.findByUnionid(f.getUnionid());
            List<CampaignOfflineCheckInLog> offlineCheckinLogs = campaignOfflineCheckInLogDaoImpl
                    .findByUnionid(f.getUnionid());
            if (answers != null && answers.size() > 0) {
                ids.addAll(answers.stream().mapToLong((m) -> m.getCampaignId()).distinct().boxed()
                        .collect(Collectors.toList()));
            }

            if (offlineCheckinLogs != null && offlineCheckinLogs.size() > 0) {
                ids.addAll(offlineCheckinLogs.stream().mapToLong((l) -> l.getCampaignId()).distinct().boxed()
                        .collect(Collectors.toList()));
            }

            boolean check = digikeyCalenderCampaignServiceImpl.checkByUnionid(f.getUnionid());
            if (check) {
                // for digikey calender campaign
                ids.add(9L);
            }

        } else {
            List<CampaignAnswer> answers = campaignAnswerDaoImpl.findByOpenidAndWechatPublicNoId(openid, channelId);
            List<CampaignOfflineCheckInLog> offlineCheckinLogs = campaignOfflineCheckInLogDaoImpl
                    .findByOpenidAndWechatPublicNoId(openid, channelId);
            if (answers != null && answers.size() > 0) {
                ids.addAll(answers.stream().mapToLong((m) -> m.getCampaignId()).distinct().boxed()
                        .collect(Collectors.toList()));
            }

            if (offlineCheckinLogs != null && offlineCheckinLogs.size() > 0) {
                ids.addAll(offlineCheckinLogs.stream().mapToLong((l) -> l.getCampaignId()).distinct().boxed()
                        .collect(Collectors.toList()));
            }

            boolean check = digikeyCalenderCampaignServiceImpl.check(openid);
            if (check) {
                // for digikey calender campaign
                ids.add(9L);
            }
        }

        if (ids.size() > 0) {
            List<Campaign> campaigns = (List<Campaign>) campaignDaoImpl.findAll(ids);
            campaigns.sort((a, b) -> {
                return a.getId() > b.getId() ? -1 : 1;
            });
            return campaigns;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Campaign> findOfflineProgressCampaigns(Long channelId) {
        Date currentDate = new Date();
        List<Campaign> total = new ArrayList<>();
        List<Campaign> campaigns = campaignDaoImpl.findOfflineInProgressCampaign(channelId, currentDate);
        if (campaigns != null && campaigns.size() > 0) {
            total.addAll(campaigns);
        }
        List<ChannelBindConfig> configs = channelBindConfigDaoImpl.findByFrom(channelId);
        if (configs != null && configs.size() > 0) {
            for (ChannelBindConfig config : configs) {
                List<Campaign> temp = campaignDaoImpl.findOfflineInProgressCampaign(config.getTo(), currentDate);
                if (temp != null && temp.size() > 0) {
                    total.addAll(temp);
                }
            }
        }
        return total;

    }

}
