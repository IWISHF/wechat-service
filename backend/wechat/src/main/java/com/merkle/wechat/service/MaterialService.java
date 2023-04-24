package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.mpnews.Mpnews;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.asset.MaterialAssetVo;

public interface MaterialService {

    void syncMaterial(WechatPublicNo pbNo, String type) throws Exception;

    Pagination<Mpnews> searchMpnews(WechatPublicNo pbNo, String key, boolean containsMultiNews, Pageable pageable);

    Pagination<MaterialAsset> getMaterial(WechatPublicNo pbNo, String type, Pageable pageable);

    MaterialAssetVo getMaterail(WechatPublicNo pbNo, List<String> mediaIds);

}
