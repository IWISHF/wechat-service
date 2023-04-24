package com.merkle.wechat.vo.autoreply;

import java.util.Date;

public class AutoReplyRuleVo<T> {
    private Long id;

    private String replyType;

    private String replyTexts;

    private String mediaId;

    private long replyArticleId;

    private T item;

    private int indexStr;

    private Date createdDate;

    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getReplyTexts() {
        return replyTexts;
    }

    public void setReplyTexts(String replyTexts) {
        this.replyTexts = replyTexts;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public long getReplyArticleId() {
        return replyArticleId;
    }

    public void setReplyArticleId(long replyArticleId) {
        this.replyArticleId = replyArticleId;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
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
