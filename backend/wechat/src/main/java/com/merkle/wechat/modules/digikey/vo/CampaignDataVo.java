package com.merkle.wechat.modules.digikey.vo;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CampaignDataVo {
    @NotEmpty(message = "name cant be empty!")
    private String name;
    @Pattern(regexp = "[0-9]{11,11}", message = "Please input correct phone number!")
    private String phone;
    @Email(message = "email not fit!")
    private String email;
    @NotEmpty(message = "address cant be empty!")
    private String address;
    @Length(max = 29, message = "blessing's max length is 28!")
    private String blessing;
    @NotEmpty(message = "openid cant be empty!")
    private String openid;
   
    private boolean isShared;

    private boolean subscribed;

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlessing() {
        return blessing;
    }

    public void setBlessing(String blessing) {
        this.blessing = blessing;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
    @JsonProperty("is_shared")
    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean isShared) {
        this.isShared = isShared;
    }

}
