package com.merkle.wechat.vo;

import com.merkle.wechat.common.annotation.NotEmpty;

public class CaptchaVerifyVo {
    @NotEmpty
    private String phone;
    @NotEmpty
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
