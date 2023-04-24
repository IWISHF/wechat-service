package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.TagGroup;

@Repository
public interface TagGroupDao extends PagingAndSortingRepository<TagGroup, Long> {

    List<TagGroup> findAllByWechatPublicNoId(Long wechatPublicNoId);

    boolean existsByNameAndWechatPublicNoId(String name, Long id);

    TagGroup findByIdAndWechatPublicNoId(Long id, Long wechatPublicNoId);

    TagGroup findByNameAndWechatPublicNoId(String defaultGroup, Long id);

    Page<TagGroup> findAllByWechatPublicNoId(Long wechatPublicNoId, Pageable pageable);

    boolean existsByNameAndWechatPublicNoIdAndIdNot(String name, Long id, Long id2);

}
