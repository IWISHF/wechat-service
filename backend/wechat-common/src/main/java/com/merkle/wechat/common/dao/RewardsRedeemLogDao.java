package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.RewardsRedeemLog;

@Repository
public interface RewardsRedeemLogDao extends PagingAndSortingRepository<RewardsRedeemLog, Long> {

    @Query(value = "select u from rewards_redeem_log u "
            + "where (u.rewardName like %?2% or u.name like %?2% or u.phone like %?2% or u.address like %?2%) "
            + "and u.rewardType=?3 and u.pubNoAppId=?1")
    Page<RewardsRedeemLog> findByConditions(String authorizerAppid, String key, String type, Pageable pageable);

    RewardsRedeemLog findByPubNoAppIdAndOpenidAndRewardIdAndCouponCode(String authorizerAppid, String openid,
            String rewardId, String code);

    List<RewardsRedeemLog> findByOpenidAndPubNoAppIdAndRedeemStatus(String openid, String authorizerAppid,
            boolean status);

    List<RewardsRedeemLog> findByOpenidAndPubNoAppIdAndRedeemStatusOrderByIdDesc(String openid, String authorizerAppid,
            boolean status);

    List<RewardsRedeemLog> findByUnionidAndRedeemStatusOrderByIdDesc(String openid, boolean b);

    RewardsRedeemLog findOneByUnionidAndRewardIdAndCouponCode(String unionid, String rewardId, String code);

    int countByRewardIdAndRedeemStatus(String rewardId, Boolean redeemStatus);

}
