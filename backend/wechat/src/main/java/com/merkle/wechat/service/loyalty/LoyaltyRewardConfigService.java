package com.merkle.wechat.service.loyalty;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.loyalty.LoyaltyRewardConfig;
import com.merkle.wechat.vo.Pagination;

public interface LoyaltyRewardConfigService {

    void create(Long channelId, LoyaltyRewardConfig config) throws Exception;

    void update(Long channelId, LoyaltyRewardConfig config) throws Exception;

    void delete(Long channelId, Long id);

    Pagination<LoyaltyRewardConfig> search(Long channelId, String key, Pageable pageable);

    LoyaltyRewardConfig findOne(Long channelId, Long id);

}
