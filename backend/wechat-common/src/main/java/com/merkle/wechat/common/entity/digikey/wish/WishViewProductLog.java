package com.merkle.wechat.common.entity.digikey.wish;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_wish_view_product_log")
public class WishViewProductLog extends BaseEntity {
    private String openid;
    private String productid;
    private long startDateTimeMills;
    private boolean recordEventStatus;
    private String recordEventErrorMessage;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
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

    public long getStartDateTimeMills() {
        return startDateTimeMills;
    }

    public void setStartDateTimeMills(long startDateTimeMills) {
        this.startDateTimeMills = startDateTimeMills;
    }

}
