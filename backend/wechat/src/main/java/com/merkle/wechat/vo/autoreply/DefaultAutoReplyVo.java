package com.merkle.wechat.vo.autoreply;

import java.util.Date;
import java.util.List;

public class DefaultAutoReplyVo {
    private Long id;

    private String name;

    private String nameKey;

    private boolean enable;

    private String toUserName;

    private String type;

    private int indexStr;

    private Long wechatPublicNoId;

    @SuppressWarnings("rawtypes")
    private List<AutoReplyRuleVo> autoReplyrules;

    private Date createdDate;

    private Date updatedDate;

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

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
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
