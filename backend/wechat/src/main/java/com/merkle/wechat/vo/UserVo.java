package com.merkle.wechat.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.merkle.wechat.vo.thridparty.WechatPublicNoVoForLogin;

public class UserVo {
    @JsonProperty("accountId")
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String language;
    private List<WechatPublicNoVoForLogin> pbNos;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<WechatPublicNoVoForLogin> getPbNos() {
        return pbNos;
    }

    public void setPbNos(List<WechatPublicNoVoForLogin> pbNos) {
        this.pbNos = pbNos;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
