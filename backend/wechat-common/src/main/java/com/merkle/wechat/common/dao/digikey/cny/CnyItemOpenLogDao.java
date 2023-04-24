package com.merkle.wechat.common.dao.digikey.cny;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.cny.CnyItemOpenLog;

@Repository
public interface CnyItemOpenLogDao extends CrudRepository<CnyItemOpenLog, Long>{

    Set<CnyItemOpenLog> findByOpenid(String openid);

}
