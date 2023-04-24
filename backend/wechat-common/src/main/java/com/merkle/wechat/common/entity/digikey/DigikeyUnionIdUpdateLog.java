package com.merkle.wechat.common.entity.digikey;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_unionid_update_log")
public class DigikeyUnionIdUpdateLog extends BaseEntity {
    private String openid;
    private String unionid;
    private String appId;
    private boolean success;
    private String errorResp;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorResp() {
        return errorResp;
    }

    public void setErrorResp(String errorResp) {
        this.errorResp = errorResp;
    }

}
