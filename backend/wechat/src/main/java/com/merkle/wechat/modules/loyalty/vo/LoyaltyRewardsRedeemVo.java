package com.merkle.wechat.modules.loyalty.vo;

import javax.validation.constraints.Pattern;

import com.merkle.wechat.common.annotation.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class LoyaltyRewardsRedeemVo {
    @NotEmpty
    @ApiModelProperty(value = "openid 非空")
    private String id;

    @NotEmpty
    @ApiModelProperty(value = "rewardId 非空")
    private String rewardId;

    @NotEmpty
    private String rewardName;

    @NotEmpty
    @Pattern(regexp = "^[0,1]")
    @ApiModelProperty(allowableValues = "0是实体,1是虚拟")
    private String rewardType;

    private String rewardGroup;

    private String phone;

    private String name;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardGroup() {
        return rewardGroup;
    }

    public void setRewardGroup(String rewardGroup) {
        this.rewardGroup = rewardGroup;
    }

}
