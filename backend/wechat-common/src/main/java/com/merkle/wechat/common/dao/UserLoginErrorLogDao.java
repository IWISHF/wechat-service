package com.merkle.wechat.common.dao;

import com.merkle.wechat.common.entity.UserLoginErrorLog;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginErrorLogDao extends CrudRepository<UserLoginErrorLog, Long> {
    List<UserLoginErrorLog> findByAccountAndTimeBetween(String paramString, long paramLong1, long paramLong2);
}
