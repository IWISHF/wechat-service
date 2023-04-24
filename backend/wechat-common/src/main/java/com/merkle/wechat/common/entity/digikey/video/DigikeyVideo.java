package com.merkle.wechat.common.entity.digikey.video;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_video")
public class DigikeyVideo extends BaseEntity {
    private String name;
    private String description;
    private String bannerPath;
    private String videoPath;
    private String referPath;
    private boolean active = true;

    @JoinColumn(name = "groupId")
    @ManyToOne(fetch = FetchType.EAGER)
    private DigikeyVideoGroup group;

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

    public String getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getReferPath() {
        return referPath;
    }

    public void setReferPath(String referPath) {
        this.referPath = referPath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DigikeyVideoGroup getGroup() {
        return group;
    }

    public void setGroup(DigikeyVideoGroup group) {
        this.group = group;
    }

}
