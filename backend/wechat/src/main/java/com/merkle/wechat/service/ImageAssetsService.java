package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.merkle.wechat.common.entity.ImageAssets;
import com.merkle.wechat.vo.Pagination;

public interface ImageAssetsService {

    String createPicture(MultipartFile pic, Long channelId) throws Exception;

    ImageAssets findImageAsset(Long channelId, Long fileId) throws Exception;

    Pagination<ImageAssets> search(Long channelId, Pageable pageable, String key);

    List<ImageAssets> getAll(Long channelId);

    void deleteImage(Long fileId, Long channelId);

}
