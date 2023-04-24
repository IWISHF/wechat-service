package com.merkle.wechat.common.dao.follower;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.follower.FollowerBindInfo;

@Repository
public interface FollowerBindInfoDao extends PagingAndSortingRepository<FollowerBindInfo, Long> {

    FollowerBindInfo findOneByOpenid(String openid);

    List<FollowerBindInfo> findBySyncToLoyalty(boolean b);

    FollowerBindInfo findOneByUnionid(String unionid);

    boolean existsByUnionid(String unionid);

    boolean existsByOpenid(String openid);

}
