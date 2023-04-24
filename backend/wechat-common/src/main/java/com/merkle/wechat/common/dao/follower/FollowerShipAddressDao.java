package com.merkle.wechat.common.dao.follower;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.follower.FollowerShipAddress;

@Repository
public interface FollowerShipAddressDao extends PagingAndSortingRepository<FollowerShipAddress, Long> {

    List<FollowerShipAddress> findAllByOpenid(String openid);

    List<FollowerShipAddress> findAllByUnionid(String unionid);

}
