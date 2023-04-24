package com.merkle.wechat.vo.autoreply;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.merkle.wechat.common.entity.TriggerTextKey;

public class KeywordsAutoReplyRuleVo {
    private Long id;

    private String name;

    private boolean enable;

    private Set<TriggerTextKey> triggerKeys;

    private String toUserName;

    private Long wechatPublicNoId;

    @SuppressWarnings("rawtypes")
    private List<AutoReplyRuleVo> autoReplyrules;

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

    @SuppressWarnings("rawtypes")
    public List<AutoReplyRuleVo> getAutoReplyrules() {
        return autoReplyrules;
    }

    @SuppressWarnings("rawtypes")
    public void setAutoReplyrules(List<AutoReplyRuleVo> autoReplyrules) {
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
