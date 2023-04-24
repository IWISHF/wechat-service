package com.merkle.wechat.vo.qrcode;

import java.util.Date;
import java.util.Set;

import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.Tag;

import io.swagger.annotations.ApiModelProperty;

public class QrcodeVo {
    @ApiModelProperty(notes = "创建不必须")
    private Long id;

    @NotEmpty
    @ApiModelProperty(required = true, notes = "必须填写")
    private String name;

    private String description;

    private Set<AutoReplyRule> autoReplyrules;

    private boolean closeSubscribeAutoReply = false;

    private boolean autoTagNewSubscribeUser = false;

    private boolean autoTagAlreadySubscribeUser = false;

    private Set<Tag> newSubscribeTags;

    private Set<Tag> alreadySubscribeTags;

    private String url;

    private String ticket;

    @ApiModelProperty(notes = "创建不必须，默认关闭")
    private boolean enable = false;

    @ApiModelProperty(notes = "创建修改不需要， 统计值占位")
    private int scanCount = 0;

    @ApiModelProperty(notes = "创建修改不需要，统计值占位")
    private int subscribeCount = 0;

    private Date updatedDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<Tag> getAlreadySubscribeTags() {
        return alreadySubscribeTags;
    }

    public void setAlreadySubscribeTags(Set<Tag> alreadySubscribeTags) {
        this.alreadySubscribeTags = alreadySubscribeTags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public boolean isAutoTagAlreadySubscribeUser() {
        return autoTagAlreadySubscribeUser;
    }

    public void setAutoTagAlreadySubscribeUser(boolean autoTagAlreadySubscribeUser) {
        this.autoTagAlreadySubscribeUser = autoTagAlreadySubscribeUser;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(int subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
