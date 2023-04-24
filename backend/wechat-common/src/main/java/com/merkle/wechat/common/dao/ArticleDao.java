package com.merkle.wechat.common.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.Article;

@Repository
public interface ArticleDao extends PagingAndSortingRepository<Article, Long> {

    Article findByWechatPublicNoIdAndId(Long wechatPublicNoId, Long articleId);

    void deleteByIdAndWechatPublicNoId(Long articleId, Long wechatPublicNoId);

    Page<Article> findByTitleContainingAndWechatPublicNoId(String key, Long wechatPublicNoId, Pageable pageable);

}
