package com.merkle.wechat.common.dao.follower;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.follower.Follower;

@Repository
public interface FollowerDao extends PagingAndSortingRepository<Follower, Long>, JpaSpecificationExecutor<Follower> {

    Follower findOneByOpenid(String openid);

    Follower findOneByOpenidAndRecordToLoyaltySuccess(String openid, boolean isSuccess);

    @Query("select distinct openid from follower where pubNoAppId=:appId and province=:province and subscribe=1")
    Set<String> getDistinctOpenIdsByAppIdAndProvince(@Param("appId") String appId, @Param("province") String province);

    Follower findOneByOpenidAndPubNoAppId(String openid, String pbNoAppId);

    Page<Follower> findByNicknameContainingAndPubNoAppId(String nickname, String pubNoAppId, Pageable pageable);

    Follower findByIdAndPubNoAppId(Long id, String appid);

    List<Follower> findByIdInAndPubNoAppId(List<Long> followerIds, String authorizerAppid);

    Follower findByOpenidAndPubNoAppId(String openid, String pubNoAppId);

    Long countByPubNoAppIdAndSubscribe(String pbNoAppId, int subscribe);

    @Query("select distinct openid from follower where pubNoAppId=:appId and subscribe=1")
    Set<String> getDistinctOpenIdsByAppId(@Param("appId") String appId);

    @Query("select distinct openid from follower where pubNoAppId=:appId and subscribe=1 and updatedDate > :startUpdatedDate")
    Set<String> getDistinctOpenIdsByAppIdAndUpdatedDate(@Param("appId") String appId,
            @Param("startUpdatedDate") Date startUpdatedDate);

    boolean existsByOpenid(String openid);

    List<Follower> findAllByPubNoAppIdAndRecordToLoyaltySuccess(String authorizerAppid, boolean b);

    @Modifying
    @Transactional
    @Query(value = "delete from follower_tag_mapping where tagId=:tagId", nativeQuery = true)
    void deleteFollowersTag(@Param("tagId") Long tagId);

    List<Follower> findAllByUnionid(String unionid);

}
