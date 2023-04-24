package com.merkle.wechat.weixin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.MaterialAssetDao;
import com.merkle.wechat.common.dao.mpnews.MpnewsArticleDao;
import com.merkle.wechat.common.dao.mpnews.MpnewsDao;
import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.mpnews.Mpnews;
import com.merkle.wechat.common.entity.mpnews.MpnewsArticle;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.Constants;
import com.merkle.wechat.service.TokenService;

import weixin.popular.api.MaterialAPI;
import weixin.popular.bean.material.MaterialBatchgetResult;
import weixin.popular.bean.material.MaterialBatchgetResultItem;
import weixin.popular.bean.material.MaterialGetResult;
import weixin.popular.bean.material.MaterialcountResult;
import weixin.popular.bean.message.Article;

@Component
public class WeixinMaterialAdapterImpl implements WeixinMaterialAdapter {
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired MaterialAssetDao materialAssetDaoImpl;
    private @Autowired MpnewsDao mpnewsDaoImpl;
    private @Autowired MpnewsArticleDao MpnewsArticleImpl;

    @Override
    public List<Mpnews> getAndUpdateMpnews(String appId, Integer offset, int count, Long wechatPublicNoId) {
        MaterialBatchgetResult result = MaterialAPI.batchget_material(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), Constants.NEWS, offset, count);
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
        // sync material
        List<MaterialBatchgetResultItem> items = result.getItem();
        List<Mpnews> mpnews = new ArrayList<Mpnews>();
        for (MaterialBatchgetResultItem item : items) {
            Mpnews news = mpnewsDaoImpl.findByWechatPublicNoIdAndMediaId(wechatPublicNoId, item.getMedia_id());
            if (news != null) {
                news.setEnable(true);
                updateExistingArticles(item, news);
                mpnews.add(news);
                continue;
            } else {
                news = new Mpnews();
            }
            news.setEnable(true);
            news.setMediaId(item.getMedia_id());
            news.setWechatPublicNoId(wechatPublicNoId);
            news.setUpdateTime(item.getUpdate_time());
            news.setUpdated(new Date());
            news.setTitle("");
            List<MpnewsArticle> mpArticles = new ArrayList<>();
            List<Article> articles = item.getContent().getNews_item();
            if (articles != null && articles.size() > 1) {
                news.setMultiMpNews(true);
            } else {
                news.setMultiMpNews(false);
            }
            for (Article a : articles) {
                MpnewsArticle mpArticle = new MpnewsArticle();
                mpArticle.setAuthor(a.getAuthor());
                mpArticle.setContentSourceUrl(a.getContent_source_url());
                mpArticle.setCreated(new Date());
                mpArticle.setDigest(a.getDigest());
                mpArticle.setNeedOpenComment(a.getNeed_open_comment());
                mpArticle.setOnlyFansCanComment(a.getOnly_fans_can_comment());
                mpArticle.setShowCoverPic(a.getShow_cover_pic());
                mpArticle.setThumbMediaid(a.getThumb_media_id());
                mpArticle.setThumbUrl(a.getThumb_url());
                mpArticle.setTitle(a.getTitle());
                news.setTitle(news.getTitle() + a.getTitle());
                mpArticle.setUrl(a.getUrl());
                mpArticles.add(mpArticle);
            }
            if (news.getArticles() != null) {
                news.getArticles().clear();
                news.getArticles().addAll(mpArticles);
            } else {
                news.setArticles(mpArticles);
            }
            mpnews.add(news);
        }
        return mpnews;
    }

    private void updateExistingArticles(MaterialBatchgetResultItem item, Mpnews news) {
        news.setUpdateTime(item.getUpdate_time());
        List<Article> articles = item.getContent().getNews_item();
        if (articles != null && articles.size() > 1) {
            news.setMultiMpNews(true);
        } else {
            news.setMultiMpNews(false);
        }
        List<MpnewsArticle> mpArticles = news.getArticles();
        for (Article a : articles) {
            List<MpnewsArticle> mpnewsArticles = MpnewsArticleImpl.findByThumbMediaidAndMpnewsId(a.getThumb_media_id(), news.getId());
            if (null == mpnewsArticles) {
                MpnewsArticle mpArticle = new MpnewsArticle();
                mpArticle.setCreated(new Date());
                mpArticle.setAuthor(a.getAuthor());
                mpArticle.setContentSourceUrl(a.getContent_source_url());
                mpArticle.setDigest(a.getDigest().toString());
                mpArticle.setNeedOpenComment(a.getNeed_open_comment());
                mpArticle.setOnlyFansCanComment(a.getOnly_fans_can_comment());
                mpArticle.setShowCoverPic(a.getShow_cover_pic());
                mpArticle.setThumbMediaid(a.getThumb_media_id());
                mpArticle.setThumbUrl(a.getThumb_url());
                mpArticle.setTitle(a.getTitle());
                news.setTitle(news.getTitle() + a.getTitle());
                mpArticle.setUrl(a.getUrl());
                mpArticle.setMpnewsId(news.getId());
                MpnewsArticleImpl.save(mpArticle);
                mpArticles.add(mpArticle);
            } else {
                mpnewsArticles.forEach((mpArticle) -> {
                    mpArticle.setAuthor(a.getAuthor());
                    mpArticle.setContentSourceUrl(a.getContent_source_url());
                    mpArticle.setDigest(a.getDigest().toString());
                    mpArticle.setNeedOpenComment(a.getNeed_open_comment());
                    mpArticle.setOnlyFansCanComment(a.getOnly_fans_can_comment());
                    mpArticle.setShowCoverPic(a.getShow_cover_pic());
                    mpArticle.setThumbMediaid(a.getThumb_media_id());
                    mpArticle.setThumbUrl(a.getThumb_url());
                    mpArticle.setTitle(a.getTitle());
                    news.setTitle(news.getTitle() + a.getTitle());
                    mpArticle.setUrl(a.getUrl());
                    mpArticle.setMpnewsId(news.getId());
                    MpnewsArticleImpl.save(mpArticle);
                    mpArticles.add(mpArticle);
                });
            }
        }
        news.setArticles(mpArticles);
    }

    @Override
    public List<MaterialAsset> getAndUpdateMaterial(String appId, String materialType, int offset, int count,
            Long wechatPublicNoId) {
        MaterialBatchgetResult result = MaterialAPI
                .batchget_material(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), materialType, offset, count);
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
        // sync material
        List<MaterialBatchgetResultItem> items = result.getItem();
        List<MaterialAsset> assets = new ArrayList<MaterialAsset>();
        items.forEach((item) -> {
            MaterialAsset asset = Optional
                    .ofNullable(
                            materialAssetDaoImpl.findByWechatPublicNoIdAndMediaId(wechatPublicNoId, item.getMedia_id()))
                    .orElseGet(() -> new MaterialAsset());
            asset.setEnable(true);
            asset.setMediaId(item.getMedia_id());
            asset.setName(item.getName());
            asset.setUpdateTime(item.getUpdate_time());
            asset.setUpdated(new Date());
            asset.setUrl(item.getUrl());
            if (StringUtils.isEmpty(item.getUrl()) && materialType.equals(Constants.VIDEO)) {
                MaterialGetResult tmp = MaterialAPI.get_material(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId),
                        asset.getMediaId());
                asset.setUrl(tmp.getDown_url());
            }
            asset.setType(materialType);
            asset.setWechatPublicNoId(wechatPublicNoId);
            if (!(StringUtils.isEmpty(asset.getUrl()) && materialType.equals(Constants.IMAGE))) {
                assets.add(asset);
            }
        });

        return assets;
    }

    @Override
    public Integer getImageTotalNo(String appId) throws Exception {
        MaterialcountResult count = getCount(appId);
        return count.getImage_count();
    }

    @Override
    public Integer getVideoTotalNo(String appId) throws Exception {
        MaterialcountResult count = getCount(appId);
        return count.getVideo_count();
    }

    @Override
    public Integer getVoiceTotalNo(String appId) throws Exception {
        MaterialcountResult count = getCount(appId);
        return count.getVoice_count();
    }

    @Override
    public Integer getMpnewsTotalNo(String appId) throws Exception {
        MaterialcountResult count = getCount(appId);
        return count.getNews_count();
    }

    private MaterialcountResult getCount(String appId) throws Exception {
        MaterialcountResult countResult = MaterialAPI
                .get_materialcount(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
        if (!countResult.isSuccess()) {
            throw new ServiceWarn(countResult.getErrmsg(), countResult.getErrcode());
        }
        return countResult;
    }

    @Override
    public Integer getTotalNo(String appId, String materialType) throws Exception {
        switch (materialType) {
            case Constants.IMAGE:
                return getImageTotalNo(appId);
            case Constants.VOICE:
                return getVoiceTotalNo(appId);
            case Constants.VIDEO:
                return getVideoTotalNo(appId);
            case Constants.NEWS:
                return getMpnewsTotalNo(appId);
        }
        return null;
    }

}
