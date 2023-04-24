package com.merkle.wechat.common.dao.mpnews;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.mpnews.Mpnews;

@Repository
public interface MpnewsDao extends PagingAndSortingRepository<Mpnews, Long> {

    List<Mpnews> findByWechatPublicNoIdAndEnable(Long wechatPublicNoId, boolean enable);

    Mpnews findByWechatPublicNoIdAndMediaId(Long wechatPublicNoId, String mediaId);

    Page<Mpnews> findByWechatPublicNoIdAndEnableAndTitleContaining(Long pbNoId, boolean enable, String key,
            Pageable pageable);

    List<Mpnews> findByWechatPublicNoIdAndMediaIdIn(Long id, List<String> mediaIds);

    Page<Mpnews> findByWechatPublicNoIdAndEnableAndMultiMpNewsAndTitleContaining(Long id, boolean enable, boolean multiMpNews,
            String key, Pageable pageable);

    Mpnews findByWechatPublicNoIdAndId(Long pbNoId, Long id);

}
