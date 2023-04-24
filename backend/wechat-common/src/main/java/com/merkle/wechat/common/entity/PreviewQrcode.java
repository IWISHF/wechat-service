package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import weixin.popular.bean.qrcode.QrcodeTicket;

@Entity(name = "preview_qrcode")
public class PreviewQrcode {
    @Transient
    public static final String PREVIEW_BATCH_TASK = "batchTaskPreivew";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String previewType;

    private boolean enable = false;

    private String toUserName;

    private Long wechatPublicNoId;

    private String description;

    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "preview_qrcodeId")
    private AutoReplyRule autoReplyrule;

    private String ticket;

    private Integer expireSeconds;

    private String url;

    private String type;

    private String appId;

    private String sceneContent;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AutoReplyRule getAutoReplyrule() {
        return autoReplyrule;
    }

    public void setAutoReplyrule(AutoReplyRule autoReplyrule) {
        this.autoReplyrule = autoReplyrule;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSceneContent() {
        return sceneContent;
    }

    public void setSceneContent(String sceneContent) {
        this.sceneContent = sceneContent;
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

    public String getPreviewType() {
        return previewType;
    }

    public void setPreviewType(String previewType) {
        this.previewType = previewType;
    }

    public static PreviewQrcode convertFromTicket(QrcodeTicket ticket) {
        PreviewQrcode code = new PreviewQrcode();
        code.setTicket(ticket.getTicket());
        code.setUrl(ticket.getUrl());
        code.setExpireSeconds(ticket.getExpire_seconds());
        return code;
    }

}
