package com.merkle.wechat.common.dao.aia;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.aia.AIAIntentionData;

@Repository
public interface AIAIntentionDataDao extends CrudRepository<AIAIntentionData, Long> {

}
