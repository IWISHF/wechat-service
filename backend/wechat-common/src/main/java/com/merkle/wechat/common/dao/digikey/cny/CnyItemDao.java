package com.merkle.wechat.common.dao.digikey.cny;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.cny.CnyItem;

@Repository
public interface CnyItemDao extends CrudRepository<CnyItem, Long> {

}
