package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.ImageAssets;

@Repository
public interface ImageAssetsDao extends PagingAndSortingRepository<ImageAssets, Long> {

    ImageAssets findByWechatPublicNoIdAndIdAndEnable(Long channelId, Long fileId, boolean enable);

    Page<ImageAssets> findByFileNameContainingAndWechatPublicNoIdAndEnable(String key, Long channelId, boolean enable,
            Pageable pageable);

    List<ImageAssets> findByWechatPublicNoIdAndEnable(Long channelId, boolean enable);

    List<ImageAssets> findByWechatPublicNoIdAndEnableOrderByCreatedDateDesc(Long channelId, boolean enable);

    ImageAssets findByIdAndWechatPublicNoIdAndEnable(Long fileId, Long channelId, boolean enable);

}
