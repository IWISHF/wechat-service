package com.merkle.wechat.common.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.AutoReplyRule;

@Repository
public interface AutoReplyRuleDao extends PagingAndSortingRepository<AutoReplyRule, Long> {

    boolean existsByReplyArticleId(Long articleId);

}
