package com.merkle.wechat.vo.thridparty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.merkle.wechat.common.entity.WechatPublicNo;

public class WechatPublicNoVo {
    public static final String SUBSCRIPTION_NUMBER = "订阅号";
    public static final String SERVICE_NUMBER = "服务号";
    public static final String AUTHENTICATED = "已认证";
    public static final String UNAUTHORIZED = "未认证";

    private Long id;
    private String nickName;

    private String headImg;
    // 授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
    private String serviceType;

    // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，
    // 3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，
    // 5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
    private String verifyType;

    private String userName; // 授权方公众号的原始ID

    private String principalName; // 公众号的主体名称

    private String alias;

    private String qrcodeUrl;

    // businessInfo
    private Integer openStore; // 是否开通微信门店功能

    private Integer openScan; // 是否开通微信扫商品功能

    private Integer openPay; // 是否开通微信支付功能

    private Integer openCard; // 是否开通微信卡券功能

    private Integer openShake; // 是否开通微信摇一摇功能

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public Integer getOpenStore() {
        return openStore;
    }

    public void setOpenStore(Integer openStore) {
        this.openStore = openStore;
    }

    public Integer getOpenScan() {
        return openScan;
    }

    public void setOpenScan(Integer openScan) {
        this.openScan = openScan;
    }

    public Integer getOpenPay() {
        return openPay;
    }

    public void setOpenPay(Integer openPay) {
        this.openPay = openPay;
    }

    public Integer getOpenCard() {
        return openCard;
    }

    public void setOpenCard(Integer openCard) {
        this.openCard = openCard;
    }

    public Integer getOpenShake() {
        return openShake;
    }

    public void setOpenShake(Integer openShake) {
        this.openShake = openShake;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static WechatPublicNoVo convertFromWechatPublicNo(WechatPublicNo no) {
        WechatPublicNoVo vo = new WechatPublicNoVo();
        BeanUtils.copyProperties(no, vo);
        if (no.getServiceTypeInfo() == 2) {
            vo.setServiceType(SERVICE_NUMBER);
        } else {
            vo.setServiceType(SUBSCRIPTION_NUMBER);
        }

        if (no.getVerifyTypeInfo() != -1) {
            vo.setVerifyType(AUTHENTICATED);
        } else {
            vo.setVerifyType(UNAUTHORIZED);
        }
        return vo;
    }

    public static List<WechatPublicNoVo> convertToWechatPublicNoVo(List<WechatPublicNo> nos) {
        List<WechatPublicNoVo> vos = new ArrayList<>();
        for (WechatPublicNo no : nos) {
            vos.add(convertFromWechatPublicNo(no));
        }
        return vos;
    }
}
