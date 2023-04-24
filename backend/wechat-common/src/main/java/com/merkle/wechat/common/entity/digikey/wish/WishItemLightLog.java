package com.merkle.wechat.common.entity.digikey.wish;

import java.util.Date;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name="digikey_wish_item_light_log")
public class WishItemLightLog extends BaseEntity {
    private String openid;
    private Long wishItemId;
    private Date lightUpDate = new Date();
    private Date canLightUpDate = new Date();
    private boolean perfect;
    private boolean recordEventStatus;
    private String recordEventErrorMessage;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getLightUpDate() {
        return lightUpDate;
    }

    public void setLightUpDate(Date lightUpDate) {
        this.lightUpDate = lightUpDate;
    }

    public Date getCanLightUpDate() {
        return canLightUpDate;
    }

    public void setCanLightUpDate(Date canLightUpDate) {
        this.canLightUpDate = canLightUpDate;
    }

    public boolean isPerfect() {
        return perfect;
    }

    public void setPerfect(boolean perfect) {
        this.perfect = perfect;
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

    public Long getWishItemId() {
        return wishItemId;
    }

    public void setWishItemId(Long wishItemId) {
        this.wishItemId = wishItemId;
    }

}
