package com.merkle.wechat.service.follower;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.dao.follower.FollowerShipAddressDao;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerShipAddress;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;

@Component
public class FollowerShipAddressServiceImpl implements FollowerShipAddressService {
    private @Autowired FollowerShipAddressDao followerShipAddressDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;

    @Override
    public List<FollowerShipAddress> getByOpenid(String openid) throws Exception {
        Follower f = followerDaoImpl.findOneByOpenid(openid);
        if (f != null && StringUtils.isNotEmpty(f.getUnionid())) {
            return followerShipAddressDaoImpl.findAllByUnionid(f.getUnionid());
        }
        return followerShipAddressDaoImpl.findAllByOpenid(openid);
    }

    @Override
    public void create(FollowerShipAddress address) throws Exception {
        Follower f = followerDaoImpl.findOneByOpenid(address.getOpenid());
        if (f != null) {
            address.setUnionid(f.getUnionid());
        }
        followerShipAddressDaoImpl.save(address);
    }

    @Override
    public void update(Long id, FollowerShipAddress address) throws Exception {
        FollowerShipAddress dbAddress = followerShipAddressDaoImpl.findOne(id);
        if (dbAddress == null) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        Follower f = followerDaoImpl.findOneByOpenid(address.getOpenid());
        if (f != null) {
            dbAddress.setUnionid(f.getUnionid());
        }
        dbAddress.setName(address.getName());
        dbAddress.setPhone(address.getPhone());
        dbAddress.setAddress(address.getAddress());
        dbAddress.setCountry(address.getCountry());
        dbAddress.setProvince(address.getProvince());
        dbAddress.setCity(address.getCity());
        dbAddress.setDistrict(address.getDistrict());
        dbAddress.setUpdatedDate(new Date());
        followerShipAddressDaoImpl.save(dbAddress);
    }

    @Override
    public void delete(Long id) {
        followerShipAddressDaoImpl.delete(id);
    }

    @Override
    public FollowerShipAddress getById(Long id) {
        return followerShipAddressDaoImpl.findOne(id);
    }

}
