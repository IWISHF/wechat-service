package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.MaterialAssetDao;
import com.merkle.wechat.common.dao.mpnews.MpnewsDao;
import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.mpnews.Mpnews;
import com.merkle.wechat.constant.Constants;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.asset.MaterialAssetVo;
import com.merkle.wechat.weixin.WeixinMaterialAdapter;

@Component
public class MaterialServiceImpl implements MaterialService {
    protected Logger logger = LoggerFactory.getLogger("MaterialServiceImpl");

    private @Autowired WeixinMaterialAdapter weixinMaterialAdapterImpl;
    private @Autowired MaterialAssetDao materialAssetDaoImpl;
    private @Autowired MpnewsDao mpnewsDaoImpl;

    @Override
    public Pagination<MaterialAsset> getMaterial(WechatPublicNo pbNo, String type, Pageable pageable) {
        Page<MaterialAsset> page = materialAssetDaoImpl.findByWechatPublicNoIdAndEnableAndType(pbNo.getId(), true, type,
                pageable);
        return new Pagination<>(page);
    }

    @Override
    public Pagination<Mpnews> searchMpnews(WechatPublicNo pbNo, String key, boolean containsMultiNews,
            Pageable pageable) {

        Page<Mpnews> page = null;
        if (containsMultiNews) {
            page = mpnewsDaoImpl.findByWechatPublicNoIdAndEnableAndTitleContaining(pbNo.getId(), true, key, pageable);
        } else {
            page = mpnewsDaoImpl.findByWechatPublicNoIdAndEnableAndMultiMpNewsAndTitleContaining(pbNo.getId(), true,
                    false, key, pageable);
        }
        Pagination<Mpnews> pagination = new Pagination<Mpnews>(page);

        return pagination;
    }

    @Override
    public void syncMaterial(WechatPublicNo pbNo, String type) throws Exception {
        switch (type) {
            case Constants.IMAGE:
                syncImage(pbNo);
                break;
            case Constants.VOICE:
                syncVoice(pbNo);
                break;
            case Constants.VIDEO:
                syncVideo(pbNo);
                break;
            case Constants.NEWS:
                syncMpnews(pbNo);
                break;
        }
    }

    private void syncImage(WechatPublicNo pbNo) throws Exception {
        syncMaterial(Constants.IMAGE, pbNo);
    }

    private void syncVoice(WechatPublicNo pbNo) throws Exception {
        syncMaterial(Constants.VOICE, pbNo);
    }

    private void syncMpnews(WechatPublicNo pbNo) throws Exception {
        String appId = pbNo.getAuthorizerAppid();

        Integer totalNo = weixinMaterialAdapterImpl.getMpnewsTotalNo(appId);

        if (totalNo == 0) {
            return;
        }

        Integer offset = 0;
        int count = 20;

        if (totalNo - offset >= 20) {
            count = 20;
        } else {
            count = totalNo - offset;
        }
        // update material to unenable
        List<Mpnews> dbnews = mpnewsDaoImpl.findByWechatPublicNoIdAndEnable(pbNo.getId(), true);

        dbnews.forEach((news) -> {
            news.setEnable(false);
            news.setUpdated(new Date());
        });
        mpnewsDaoImpl.save(dbnews);

        while (offset < totalNo) {
            logger.info("=== sync Mpnews Material total:" + totalNo + " from:" + offset + " to:" + (offset + count)
                    + " ===");
            List<Mpnews> mpnews = weixinMaterialAdapterImpl.getAndUpdateMpnews(appId, offset, count, pbNo.getId());
            mpnewsDaoImpl.save(mpnews);

            offset += count;
            if (totalNo - offset >= 20) {
                count = 20;
            } else {
                count = totalNo - offset;
            }

        }
    }

    private void syncVideo(WechatPublicNo pbNo) throws Exception {
        syncMaterial(Constants.VIDEO, pbNo);
    }

    private void syncMaterial(String materialType, WechatPublicNo pbNo) throws Exception {
        String appId = pbNo.getAuthorizerAppid();

        Integer totalNo = weixinMaterialAdapterImpl.getTotalNo(appId, materialType);

        if (totalNo == 0) {
            return;
        }

        Integer offset = 0;
        int count = 20;

        if (totalNo - offset >= 20) {
            count = 20;
        } else {
            count = totalNo - offset;
        }

        // update material to unenable
        List<MaterialAsset> dbAssets = materialAssetDaoImpl.findByWechatPublicNoIdAndEnableAndType(pbNo.getId(), true,
                materialType);
        dbAssets.forEach((asset) -> {
            asset.setEnable(false);
            asset.setUpdated(new Date());
        });
        materialAssetDaoImpl.save(dbAssets);

        while (offset < totalNo) {
            logger.info("=== sync " + materialType + " Material total:" + totalNo + " from:" + offset + " to:"
                    + (offset + count) + " ===");
            List<MaterialAsset> assets = weixinMaterialAdapterImpl.getAndUpdateMaterial(appId, materialType, offset,
                    count, pbNo.getId());
            materialAssetDaoImpl.save(assets);

            offset += count;
            if (totalNo - offset >= 20) {
                count = 20;
            } else {
                count = totalNo - offset;
            }

        }
    }

    @Override
    public MaterialAssetVo getMaterail(WechatPublicNo pbNo, List<String> mediaIds) {
        MaterialAssetVo vo = new MaterialAssetVo();
        vo.setMaterials(materialAssetDaoImpl.findByWechatPublicNoIdAndEnableAndMediaIdIn(pbNo.getId(), true, mediaIds));
        vo.setMpNews(mpnewsDaoImpl.findByWechatPublicNoIdAndMediaIdIn(pbNo.getId(), mediaIds));
        return vo;
    }

}
