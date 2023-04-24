package com.merkle.wechat.common.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.merkle.wechat.common.entity.RefreshFlag;

@Repository
public interface RefreshFlagDao extends CrudRepository<RefreshFlag, Long> {

    @Transactional
    void removeByAppId(String appId);

    RefreshFlag findByAppId(String appId);

    RefreshFlag findByAppIdAndCreatedDateLessThan(String appId, Date date);

    @Transactional
    void removeByTaskId(Long taskId);

    RefreshFlag findByTaskId(Long taskId);

    RefreshFlag findByTaskIdAndCreatedDateLessThan(Long taskId, Date date);

}
