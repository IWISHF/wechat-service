package com.merkle.wechat.common.entity.mpnews;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "mpnews_article")
public class MpnewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "varchar(500) default null")
    private String title;

    private String thumbMediaid;// 图文消息的封面图片素材id

    private String showCoverPic;// 是否显示封面，0为false，即不显示，1为true，即显示

    private String thumbUrl; // 封面图片地址

    private String author;

    private String digest;// 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;// 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS

    private String url; // 图文页的URL(高级群发不可用外链)

    @Column(columnDefinition = "text")
    private String contentSourceUrl; // 图文消息的原文地址，即点击“阅读原文”后的URL

    private Integer needOpenComment; // 是否打开评论，0不打开，1打开

    private Integer onlyFansCanComment; // 是否粉丝才可评论，0所有人可评论，1粉丝才可评论

    private Date created = new Date();

    private Long mpnewsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbMediaid() {
        return thumbMediaid;
    }

    public void setThumbMediaid(String thumbMediaid) {
        this.thumbMediaid = thumbMediaid;
    }

    public String getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(String showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public Integer getNeedOpenComment() {
        return needOpenComment;
    }

    public void setNeedOpenComment(Integer needOpenComment) {
        this.needOpenComment = needOpenComment;
    }

    public Integer getOnlyFansCanComment() {
        return onlyFansCanComment;
    }

    public void setOnlyFansCanComment(Integer onlyFansCanComment) {
        this.onlyFansCanComment = onlyFansCanComment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getMpnewsId() {
        return mpnewsId;
    }

    public void setMpnewsId(Long mpnewsId) {
        this.mpnewsId = mpnewsId;
    }

}
