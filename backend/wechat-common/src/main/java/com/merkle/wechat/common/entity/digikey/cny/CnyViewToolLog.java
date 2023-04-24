package com.merkle.wechat.common.entity.digikey.cny;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_cny_view_tool_log")
public class CnyViewToolLog extends BaseEntity {
    private String openid;
    private String toolid;
    private long startDateTimeMills;
    private boolean recordEventStatus;
    private String recordEventErrorMessage;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getToolid() {
        return toolid;
    }

    public void setToolid(String toolid) {
        this.toolid = toolid;
    }

    public long getStartDateTimeMills() {
        return startDateTimeMills;
    }

    public void setStartDateTimeMills(long startDateTimeMills) {
        this.startDateTimeMills = startDateTimeMills;
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
