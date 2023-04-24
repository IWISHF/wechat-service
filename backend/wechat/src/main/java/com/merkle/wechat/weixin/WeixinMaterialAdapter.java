package com.merkle.wechat.weixin;

import java.util.List;

import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.mpnews.Mpnews;

public interface WeixinMaterialAdapter {

    Integer getVoiceTotalNo(String appId) throws Exception;

    Integer getMpnewsTotalNo(String appId) throws Exception;

    Integer getVideoTotalNo(String appId) throws Exception;

    Integer getImageTotalNo(String appId) throws Exception;

    List<MaterialAsset> getAndUpdateMaterial(String appId, String materialType, int offset, int count, Long channelId);

    Integer getTotalNo(String appId, String materialType) throws Exception;

    List<Mpnews> getAndUpdateMpnews(String appId, Integer offset, int count, Long id) throws Exception;

}
