package com.merkle.wechat.vo.mini;

public class DecryptDataVo {
    private String encryptData;
    private String iv;

    public String getEncryptData() {
        return encryptData;
    }

    public void setEncryptData(String encryptData) {
        this.encryptData = encryptData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

}
