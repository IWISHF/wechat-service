package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.MaterialAsset;

@Repository
public interface MaterialAssetDao extends PagingAndSortingRepository<MaterialAsset, Long> {

    List<MaterialAsset> findByWechatPublicNoIdAndEnable(Long wechatPublicNoId, boolean enable);

    MaterialAsset findByWechatPublicNoIdAndMediaId(Long id, String media_id);

    List<MaterialAsset> findByWechatPublicNoIdAndEnableAndType(Long wechatPublicNoId, boolean enable, String type);

    Page<MaterialAsset> findByWechatPublicNoIdAndEnableAndType(Long pbNoId, boolean enable, String type,
            Pageable pageable);

    List<MaterialAsset> findByWechatPublicNoIdAndEnableAndMediaIdIn(Long pbNoId, boolean enable, List<String> mediaIds);

}
