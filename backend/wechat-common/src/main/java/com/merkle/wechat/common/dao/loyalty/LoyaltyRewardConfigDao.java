package com.merkle.wechat.common.dao.loyalty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.loyalty.LoyaltyRewardConfig;

@Repository
public interface LoyaltyRewardConfigDao extends PagingAndSortingRepository<LoyaltyRewardConfig, Long> {

    LoyaltyRewardConfig findByWechatPublicNoIdAndRewardId(Long channelId, int rewardId);

    void deleteByWechatPublicNoIdAndId(Long channelId, Long id);

    Page<LoyaltyRewardConfig> findByWechatPublicNoIdAndRewardNameContaining(Long channelId, String key,
            Pageable pageable);

    LoyaltyRewardConfig findByAppIdAndRewardId(String pubNoAppId, int rewardId);

    LoyaltyRewardConfig findOneByWechatPublicNoIdAndId(Long channelId, Long id);

}
