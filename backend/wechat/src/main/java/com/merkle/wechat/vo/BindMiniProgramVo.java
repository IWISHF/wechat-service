package com.merkle.wechat.vo;

import com.merkle.wechat.common.annotation.NotEmpty;

public class BindMiniProgramVo {
    @NotEmpty
    private String appId;
    @NotEmpty
    private String appSecret;
    @NotEmpty
    private String nickName;
    @NotEmpty
    private String userName;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
