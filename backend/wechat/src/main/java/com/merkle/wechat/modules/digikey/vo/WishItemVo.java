package com.merkle.wechat.modules.digikey.vo;

import static com.merkle.wechat.modules.digikey.service.WishServiceImpl.phase1Start;
import static com.merkle.wechat.modules.digikey.service.WishServiceImpl.phase2Start;
import static com.merkle.wechat.modules.digikey.service.WishServiceImpl.phase3Start;

import java.util.Date;

import com.merkle.wechat.common.util.TimeUtil;

public class WishItemVo {
    private Long id;
    private String name;
    private String description;
    private String picUrl;
    private String pageUrl;
    private Date enableStartDate;
    private boolean alreadyLightUp;
    private Date lightUpDate;
    private boolean perfect;
    private boolean canLightUp;
    private boolean canSee;
    private String currentPhase;
    private String customData;
    private boolean shared;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Date getEnableStartDate() {
        return enableStartDate;
    }

    public void setEnableStartDate(Date enableStartDate) {
        this.enableStartDate = enableStartDate;
        long startOfDayTime = TimeUtil.getTodayStart().getTime();
        long enableStartTime = enableStartDate.getTime();

        if (startOfDayTime >= phase3Start()) {
            currentPhase = "3";
            canSee = true;
            canLightUp = true;
        } else if (startOfDayTime >= phase2Start()) {
            currentPhase = "2";
            if (enableStartTime < phase3Start()) {
                canSee = true;
                canLightUp = true;
            }
        } else if (startOfDayTime >= phase1Start()) {
            currentPhase = "1";
            if (enableStartTime < phase2Start()) {
                canSee = true;
                canLightUp = true;
            }
        } else {
            currentPhase = "0";
        }
    }

    public boolean isAlreadyLightUp() {
        return alreadyLightUp;
    }

    public void setAlreadyLightUp(boolean alreadyLightUp) {
        this.alreadyLightUp = alreadyLightUp;
    }

    public Date getLightUpDate() {
        return lightUpDate;
    }

    public void setLightUpDate(Date lightUpDate) {
        this.lightUpDate = lightUpDate;
    }

    public boolean isPerfect() {
        return perfect;
    }

    public void setPerfect(boolean perfect) {
        this.perfect = perfect;
    }

    public boolean isCanLightUp() {
        return canLightUp;
    }

    public boolean isCanSee() {
        return canSee;
    }

    public void setCanLightUp(boolean canLightUp) {
        this.canLightUp = canLightUp;
    }

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

}
