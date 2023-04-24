package com.merkle.wechat.service.follower;

import java.util.List;

import com.merkle.wechat.common.entity.follower.FollowerShipAddress;

public interface FollowerShipAddressService {

    List<FollowerShipAddress> getByOpenid(String openid) throws Exception;

    void create(FollowerShipAddress address) throws Exception;

    void update(Long id, FollowerShipAddress address) throws Exception;

    void delete(Long id);

    FollowerShipAddress getById(Long id);

}
