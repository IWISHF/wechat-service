package com.merkle.wechat.common.dao.loyalty;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.loyalty.LoyaltyConfig;

@Repository
public interface LoyaltyConfigDao extends PagingAndSortingRepository<LoyaltyConfig, Long> {

    LoyaltyConfig findByWechatPublicNoId(Long pbNoId);

    LoyaltyConfig findByAppId(String appId);
}
