package com.merkle.wechat.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import weixin.popular.bean.qrcode.QrcodeTicket;

@Entity(name = "qrcode")
public class Qrcode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean enable = false;

    private String toUserName;

    private Long wechatPublicNoId;

    private String description;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "qrcodeId")
    private Set<AutoReplyRule> autoReplyrules = new HashSet<>();

    private boolean closeSubscribeAutoReply = false;

    private boolean autoTagNewSubscribeUser = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "qrcode_new_subscribe_tag_mapping", joinColumns = @JoinColumn(name = "qrcodeId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "id"))
    private Set<Tag> newSubscribeTags = new HashSet<>();

    private boolean autoTagAlreadySubscribeUser = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "qrcode_already_subscribe_tag_mapping", joinColumns = @JoinColumn(name = "qrcodeId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "id"))
    private Set<Tag> alreadySubscribeTags = new HashSet<>();

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    public Set<AutoReplyRule> getAutoReplyrules() {
        return autoReplyrules;
    }

    public void setAutoReplyrules(Set<AutoReplyRule> autoReplyrules) {
        this.autoReplyrules = autoReplyrules;
    }

    public boolean isCloseSubscribeAutoReply() {
        return closeSubscribeAutoReply;
    }

    public void setCloseSubscribeAutoReply(boolean closeSubscribeAutoReply) {
        this.closeSubscribeAutoReply = closeSubscribeAutoReply;
    }

    public boolean isAutoTagNewSubscribeUser() {
        return autoTagNewSubscribeUser;
    }

    public void setAutoTagNewSubscribeUser(boolean autoTagNewSubscribeUser) {
        this.autoTagNewSubscribeUser = autoTagNewSubscribeUser;
    }

    public Set<Tag> getNewSubscribeTags() {
        return newSubscribeTags;
    }

    public void setNewSubscribeTags(Set<Tag> newSubscribeTags) {
        this.newSubscribeTags = newSubscribeTags;
    }

    public boolean isAutoTagAlreadySubscribeUser() {
        return autoTagAlreadySubscribeUser;
    }

    public void setAutoTagAlreadySubscribeUser(boolean autoTagAlreadySubscribeUser) {
        this.autoTagAlreadySubscribeUser = autoTagAlreadySubscribeUser;
    }

    public Set<Tag> getAlreadySubscribeTags() {
        return alreadySubscribeTags;
    }

    public void setAlreadySubscribeTags(Set<Tag> alreadySubscribeTags) {
        this.alreadySubscribeTags = alreadySubscribeTags;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public static Qrcode convertFromTicket(QrcodeTicket ticket) {
        Qrcode code = new Qrcode();
        code.setTicket(ticket.getTicket());
        code.setUrl(ticket.getUrl());
        code.setExpireSeconds(ticket.getExpire_seconds());
        return code;
    }

}
