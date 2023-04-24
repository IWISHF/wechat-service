package com.merkle.wechat.common.dao.mpnews;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.mpnews.MpnewsArticle;

@Repository
public interface MpnewsArticleDao extends PagingAndSortingRepository<MpnewsArticle, Long> {

    MpnewsArticle findOneByThumbMediaidAndMpnewsId(String thumbMediaid, Long mpnewsId);

    List<MpnewsArticle> findByThumbMediaidAndMpnewsId(String thumbMediaid, Long mpnewsId);
}
