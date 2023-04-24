package com.merkle.wechat.modules.aia.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.merkle.wechat.common.annotation.NotEmpty;

public class AIAUserInfoVo {
    @NotEmpty
    private String openid;

    @NotEmpty
    @Pattern(regexp = "[0-9]{11}")
    @Length(min = 11, max = 11)
    private String phone;
    @NotNull
    private Long channelId;
    @NotEmpty
    private String city;
    @NotEmpty
    private String district;
    @NotEmpty
    private String province;

    @NotEmpty
    @Pattern(regexp = "[0-9]{6}")
    @Length(min = 6, max = 6)
    private String code;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
