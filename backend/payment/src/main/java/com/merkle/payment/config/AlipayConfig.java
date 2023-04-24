package com.merkle.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {
    @Value("${alipay.app.appid}")
    private String appid;
    @Value("${alipay.app.private_key}")
    private String privateKey;
    @Value("${alipay.app.url}")
    private String url;
    @Value("${alipay.app.public_key}")
    private String publicKey;
    @Value("${alipay.app.charset}")
    private String charset;
    @Value("${alipay.app.signtype}")
    private String signtype;
    @Value("${alipay.app.format}")
    private String format;
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getPrivateKey() {
        return privateKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public String getSigntype() {
        return signtype;
    }
    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
}
