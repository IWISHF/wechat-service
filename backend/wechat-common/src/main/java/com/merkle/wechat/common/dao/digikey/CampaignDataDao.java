package com.merkle.wechat.common.dao.digikey;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.CampaignData;

@Repository
public interface CampaignDataDao extends CrudRepository<CampaignData, Long> {
    CampaignData findOneByOpenid(String openid);
}
