package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.TagGroupCondition;

@Repository
public interface TagGroupConditionDao extends CrudRepository<TagGroupCondition, Long> {

}
