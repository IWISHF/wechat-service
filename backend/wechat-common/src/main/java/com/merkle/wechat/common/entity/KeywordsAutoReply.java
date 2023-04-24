package com.merkle.wechat.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "keywords_auto_reply")
public class KeywordsAutoReply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "创建时不必须")
    private Long id;

    private String name;

    @ApiModelProperty(notes = "创建时不必须")
    private boolean enable;

    @ApiModelProperty(notes = "创建时必须")
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "keywordsAutoReplyId")
    @OrderBy(value = "indexStr asc")
    private Set<TriggerTextKey> triggerKeys;

    @ApiModelProperty(notes = "创建时不必须")
    private String toUserName;

    @ApiModelProperty(notes = "创建时不必须")
    private Long wechatPublicNoId;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "keywordsAutoReplyId")
    @OrderBy(value = "indexStr asc")
    private Set<AutoReplyRule> autoReplyrules;

    @ApiModelProperty(notes = "创建时不必须")
    private Date createdDate = new Date();

    @ApiModelProperty(notes = "创建时不必须")
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

    public Set<TriggerTextKey> getTriggerKeys() {
        return triggerKeys;
    }

    public void setTriggerKeys(Set<TriggerTextKey> triggerKeys) {
        this.triggerKeys = triggerKeys;
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

    public Set<AutoReplyRule> getAutoReplyrules() {
        return autoReplyrules;
    }

    public void setAutoReplyrules(Set<AutoReplyRule> autoReplyrules) {
        this.autoReplyrules = autoReplyrules;
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
