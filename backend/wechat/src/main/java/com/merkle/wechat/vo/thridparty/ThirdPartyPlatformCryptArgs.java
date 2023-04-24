package com.merkle.wechat.vo.thridparty;

public class ThirdPartyPlatformCryptArgs {
    public static final String SECRET = "secret";
    public static final String TOKEN = "token";
    public static final String ENCODING_AES_KEY = "encodingAESKey";

    private String secret;
    private String token;
    private String encodingAESKey;
    private String encryptAppId;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    public String getEncryptAppId() {
        return encryptAppId;
    }

    public void setEncryptAppId(String encryptAppId) {
        this.encryptAppId = encryptAppId;
    }

}
