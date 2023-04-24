package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.merkle.wechat.common.constant.CaptchaStatusConstant;

@Entity(name = "ali_captcha")
public class AliCaptcha extends BaseEntity {
    private String openid;
    private String appId;
    private String code;
    private Date expiredTime;
    private String phone;
    private CaptchaStatusConstant status;
    private String requestId;
    private String message;
    private String bizId;
    private String respCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CaptchaStatusConstant getStatus() {
        return status;
    }

    public void setStatus(CaptchaStatusConstant status) {
        this.status = status;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

}
