package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.WeixinServerIp;

@Repository
public interface WeixinServerIpDao extends CrudRepository<WeixinServerIp, Long> {

}
