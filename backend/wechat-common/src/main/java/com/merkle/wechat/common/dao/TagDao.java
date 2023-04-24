package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.TagGroup;

@Repository
public interface TagDao extends PagingAndSortingRepository<Tag, Long> {

    List<Tag> findByGroup(TagGroup group);

    List<Tag> findByWechatPublicNoIdAndFromWechat(Long id, boolean b);

    Tag findByNameAndWechatPublicNoId(String name, Long id);

    Tag findByIdAndWechatPublicNoId(Long id, Long pbNoId);

    List<Tag> findByNameAndWechatPublicNoIdAndFromWechat(String name, Long id, boolean fromWechat);

    Page<Tag> findByNameContainingAndWechatPublicNoIdAndFromWechat(String name, Long id, boolean fromWechat,
            Pageable pageable);

    Tag findByTagIdAndWechatPublicNoId(Long tagId, Long pbNoId);

    Page<Tag> findByNameContainingAndGroup_IdAndWechatPublicNoIdAndFromWechat(String name, Long groupId, Long id,
            boolean b, Pageable pageable);

    Page<Tag> findByNameContainingAndWechatPublicNoId(String name, Long id, Pageable pageable);

    Page<Tag> findByNameContainingAndGroup_IdAndWechatPublicNoId(String name, Long groupId, Long id, Pageable pageable);

    boolean existsByNameAndWechatPublicNoIdAndIdNot(String name, Long pbNoId, Long id);

    List<Tag> findByIdInAndWechatPublicNoId(long[] tagIds, Long id);

    Page<Tag> findByNameAndWechatPublicNoIdAndFromWechatOrderByCountDesc(String key, Long pbNoId, boolean fromWechat,
            Pageable pageable);

    Page<Tag> findByNameContainingAndWechatPublicNoIdOrderByCountDesc(String key, Long id, Pageable pageable);

}
