package com.merkle.wechat.common.entity.digikey.wish;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_wish_share_log")
public class ShareLog extends BaseEntity {
    private String openid;
    private String content;
    private String type;
    private boolean reachLimit;
    private boolean recordEventStatus;
    private String recordEventErrorMessage;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRecordEventStatus() {
        return recordEventStatus;
    }

    public void setRecordEventStatus(boolean recordEventStatus) {
        this.recordEventStatus = recordEventStatus;
    }

    public String getRecordEventErrorMessage() {
        return recordEventErrorMessage;
    }

    public void setRecordEventErrorMessage(String recordEventErrorMessage) {
        this.recordEventErrorMessage = recordEventErrorMessage;
    }

    public boolean isReachLimit() {
        return reachLimit;
    }

    public void setReachLimit(boolean reachLimit) {
        this.reachLimit = reachLimit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
