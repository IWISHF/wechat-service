package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.AnnualPartyFlag;

@Repository
public interface AnnualFlagDao extends CrudRepository<AnnualPartyFlag, Long> {
    AnnualPartyFlag findOneByType(String type);
}
