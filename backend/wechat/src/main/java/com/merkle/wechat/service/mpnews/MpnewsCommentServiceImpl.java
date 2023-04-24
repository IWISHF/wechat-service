package com.merkle.wechat.service.mpnews;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.mpnews.MpnewsCommentDao;
import com.merkle.wechat.common.dao.mpnews.MpnewsDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.mpnews.Mpnews;
import com.merkle.wechat.common.entity.mpnews.MpnewsArticle;
import com.merkle.wechat.common.entity.mpnews.MpnewsComment;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenServiceImpl;

import weixin.popular.api.CommentAPI;
import weixin.popular.bean.comment.Comment;
import weixin.popular.bean.comment.CommentList;
import weixin.popular.bean.comment.CommentListResult;
import weixin.popular.util.EmojiUtil;

@Component
public class MpnewsCommentServiceImpl implements MpnewsCommentService {

    private @Autowired TokenServiceImpl tokenServiceImpl;

    private @Autowired MpnewsCommentDao mpnewsCommentDaoImpl;

    private @Autowired MpnewsDao mpnewsDaoImpl;

    @Transactional
    @Override
    public void syncMpnewsComments(Long mpnewsId, WechatPublicNo pbNo, Long msgDataId) {
        Mpnews mpnews = mpnewsDaoImpl.findByWechatPublicNoIdAndId(pbNo.getId(), mpnewsId);
        if (mpnews == null) {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }
        if (!mpnews.isMultiMpNews()) {
            int begin = 0;
            int count = 49;
            int wxLimit = 49;

            CommentList cl = new CommentList();
            cl.setCount(count);
            cl.setMsg_data_id(msgDataId);
            cl.setBegin(begin);
            CommentListResult result = CommentAPI
                    .list(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), cl);
            if (result.isSuccess()) {
                Long total = result.getTotal();
                if (total < 1) {
                    return;
                }
                List<Comment> wxComments = result.getComment();
                List<MpnewsComment> needToSave = convertCommentsToMpnewsComments(wxComments, mpnewsId,
                        mpnews.getArticles().get(0).getId());
                mpnewsCommentDaoImpl.save(needToSave);

                if (total > wxLimit) {
                    begin = begin + wxLimit;
                    while (begin < total) {
                        cl.setBegin(begin);
                        result = CommentAPI
                                .list(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), cl);
                        if (result.isSuccess()) {
                            wxComments = result.getComment();
                            needToSave = convertCommentsToMpnewsComments(wxComments, mpnewsId,
                                    mpnews.getArticles().get(0).getId());
                            mpnewsCommentDaoImpl.save(needToSave);
                        }
                        begin = begin + wxLimit;
                    }
                }
            } else {
                throw new ServiceWarn(result.getErrcode() + result.getErrmsg());
            }
            return;
        }

        List<MpnewsArticle> articles = mpnews.getArticles();
        int index = 0;
        for (MpnewsArticle a : articles) {
            int begin = 0;
            int count = 49;
            int wxLimit = 49;

            CommentList cl = new CommentList();
            cl.setCount(count);
            cl.setIndex(index);
            cl.setMsg_data_id(msgDataId);
            cl.setBegin(begin);
            CommentListResult result = CommentAPI
                    .list(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), cl);
            if (result.isSuccess()) {
                Long total = result.getTotal();
                if (total < 1) {
                    index++;
                    continue;
                }
                List<Comment> wxComments = result.getComment();
                List<MpnewsComment> needToSave = convertCommentsToMpnewsComments(wxComments, mpnewsId, a.getId());
                mpnewsCommentDaoImpl.save(needToSave);

                if (total > wxLimit) {
                    begin = begin + wxLimit;
                    while (begin < total) {
                        cl.setBegin(begin);
                        result = CommentAPI
                                .list(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), cl);
                        if (result.isSuccess()) {
                            wxComments = result.getComment();
                            needToSave = convertCommentsToMpnewsComments(wxComments, mpnewsId, a.getId());
                            mpnewsCommentDaoImpl.save(needToSave);
                        }
                        begin = begin + wxLimit;
                    }
                }
            } else {
                throw new ServiceWarn(result.getErrcode() + result.getErrmsg());
            }
            index++;
        }
    }

    private List<MpnewsComment> convertCommentsToMpnewsComments(List<Comment> wxComments, Long mpnewsId,
            Long mpnewsArticleId) {
        List<MpnewsComment> needToSave = new ArrayList<>();
        wxComments.forEach((c) -> {
            MpnewsComment dbComment = Optional.ofNullable(
                    mpnewsCommentDaoImpl.findByMpnewsArticleIdAndUserCommentId(mpnewsArticleId, c.getUser_comment_id()))
                    .orElseGet(() -> new MpnewsComment());
            dbComment.setMpnewsId(mpnewsId);
            dbComment.setMpnewsArticleId(mpnewsArticleId);
            dbComment.setCommentType(c.getComment_type());
            dbComment.setContent(EmojiUtil.removeAllEmojis(c.getContent()));
            dbComment.setCreateTime(c.getCreate_time());
            dbComment.setOpenid(c.getOpenid());
            dbComment.setUserCommentId(c.getUser_comment_id());
            if (c.getReply() != null) {
                dbComment.setReplyContent(EmojiUtil.removeAllEmojis(c.getReply().getContent()));
                dbComment.setReplyCreateTime(c.getReply().getCreate_time());
            }
            needToSave.add(dbComment);
        });
        return needToSave;
    }

}
