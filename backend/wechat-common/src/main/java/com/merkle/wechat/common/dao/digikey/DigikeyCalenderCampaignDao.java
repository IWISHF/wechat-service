package com.merkle.wechat.common.dao.digikey;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.DigikeyCalenderCampaign;

@Repository
public interface DigikeyCalenderCampaignDao extends PagingAndSortingRepository<DigikeyCalenderCampaign, Long> {

    boolean existsByOpenid(String openid);

    DigikeyCalenderCampaign findByOpenid(String openid);

    boolean existsByUnionid(String unionid);

}
