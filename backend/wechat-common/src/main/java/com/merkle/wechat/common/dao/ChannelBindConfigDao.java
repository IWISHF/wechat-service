package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.ChannelBindConfig;

@Repository
public interface ChannelBindConfigDao extends CrudRepository<ChannelBindConfig, Long> {

    List<ChannelBindConfig> findByFrom(Long wechatPublicNoId);

}
