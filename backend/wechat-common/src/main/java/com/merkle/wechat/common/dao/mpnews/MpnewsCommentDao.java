package com.merkle.wechat.common.dao.mpnews;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.mpnews.MpnewsComment;

@Repository
public interface MpnewsCommentDao extends PagingAndSortingRepository<MpnewsComment, Long> {

    MpnewsComment findByMpnewsArticleIdAndUserCommentId(Long mpnewsArticleId, Long user_comment_id);

}
