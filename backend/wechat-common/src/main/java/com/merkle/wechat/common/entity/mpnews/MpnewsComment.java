package com.merkle.wechat.common.entity.mpnews;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "mpnews_comment")
public class MpnewsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long mpnewsId;

    private Long mpnewsArticleId;

    private Long userCommentId;

    private String openid;

    private String createTime;

    private String content;

    // 是否精选评论，0为即非精选，1为true，即精选
    private Integer commentType;

    private String replyContent;

    private String replyCreateTime;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMpnewsId() {
        return mpnewsId;
    }

    public void setMpnewsId(Long mpnewsId) {
        this.mpnewsId = mpnewsId;
    }

    public Long getMpnewsArticleId() {
        return mpnewsArticleId;
    }

    public void setMpnewsArticleId(Long mpnewsArticleId) {
        this.mpnewsArticleId = mpnewsArticleId;
    }

    public Long getUserCommentId() {
        return userCommentId;
    }

    public void setUserCommentId(Long userCommentId) {
        this.userCommentId = userCommentId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCommentType() {
        return commentType;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

   

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyCreateTime() {
        return replyCreateTime;
    }

    public void setReplyCreateTime(String replyCreateTime) {
        this.replyCreateTime = replyCreateTime;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
