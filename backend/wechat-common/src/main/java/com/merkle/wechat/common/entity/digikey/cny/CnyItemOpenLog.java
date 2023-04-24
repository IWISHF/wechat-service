package com.merkle.wechat.common.entity.digikey.cny;

import java.util.Date;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_cny_item_open_log")
public class CnyItemOpenLog extends BaseEntity {
    private String openid;
    private Long cnyItemId;
    private Date openDate = new Date();
    private Date canOpenDate = new Date();
    private boolean isEarnPoints;
    private boolean recordEventStatus;
    private String recordEventErrorMessage;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getCnyItemId() {
        return cnyItemId;
    }

    public void setCnyItemId(Long cnyItemId) {
        this.cnyItemId = cnyItemId;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCanOpenDate() {
        return canOpenDate;
    }

    public void setCanOpenDate(Date canOpenDate) {
        this.canOpenDate = canOpenDate;
    }

    public boolean isEarnPoints() {
        return isEarnPoints;
    }

    public void setEarnPoints(boolean isEarnPoints) {
        this.isEarnPoints = isEarnPoints;
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

}
