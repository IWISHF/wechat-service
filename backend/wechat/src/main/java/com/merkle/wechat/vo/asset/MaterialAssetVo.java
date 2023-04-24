package com.merkle.wechat.vo.asset;

import java.util.List;

import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.mpnews.Mpnews;

public class MaterialAssetVo {
    List<Mpnews> mpNews;
    List<MaterialAsset> materials;

    public List<MaterialAsset> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialAsset> materials) {
        this.materials = materials;
    }

    public List<Mpnews> getMpNews() {
        return mpNews;
    }

    public void setMpNews(List<Mpnews> mpNews) {
        this.mpNews = mpNews;
    }

}
