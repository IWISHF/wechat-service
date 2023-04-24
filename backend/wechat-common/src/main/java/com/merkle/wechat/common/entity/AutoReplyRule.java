package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "autoreplyrule")
public class AutoReplyRule {
    public static final String REPLY_TEXT = "text";
    public static final String REPLY_ARTICLE = "article";
    public static final String REPLY_PICTURE = "picture";
    public static final String REPLY_VIDEO = "video";
    public static final String REPLY_MPNEWS = "mpnews";
    public static final String REPLY_AUTO_TAG = "autoTag";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(allowableValues = "text,article,picture,video,mpnews,autoTag")
    private String replyType;

    @ApiModelProperty(notes = "当replyType是text时使用")
    private String replyTexts;
    @ApiModelProperty(notes = "当replyType是picture，video，image，mpnews时使用")
    private String mediaId;
    @ApiModelProperty(notes = "当replyType是article高级图文时使用")
    private long replyArticleId;

    private int indexStr;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

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

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
    }

}
