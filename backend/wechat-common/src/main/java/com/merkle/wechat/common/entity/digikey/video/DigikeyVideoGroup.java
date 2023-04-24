package com.merkle.wechat.common.entity.digikey.video;

import javax.persistence.Entity;

import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "digikey_video_group")
public class DigikeyVideoGroup extends BaseEntity {
    @NotEmpty
    @ApiModelProperty(notes = "不可重复")
    private String name;
    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
