package com.merkle.wechat.common.dao.digikey;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.DigikeyUnionIdUpdateLog;

@Repository
public interface DigikeyUnionIdUpdateLogDao extends CrudRepository<DigikeyUnionIdUpdateLog, Long> {

}
