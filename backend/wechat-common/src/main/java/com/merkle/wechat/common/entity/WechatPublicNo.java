package com.merkle.wechat.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.merkle.wechat.common.util.JSONUtil;

import weixin.popular.bean.component.FuncInfo;

@Entity(name = "wechatpublicno")
public class WechatPublicNo {
    public static final String NOT_AUTH = "未授权";
    public static final String ALREADY_AUTH = "已授权";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long accountId;

    private String authorizerAppid;
    // Json
    @Column(columnDefinition = "text")
    private String funcInfo;

    @Transient
    private List<FuncInfo> funcInfoList;

    private String nickName;

    private String headImg;
    // 授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
    private int serviceTypeInfo;

    // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，
    // 3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，
    // 5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
    private int verifyTypeInfo;

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

    private String miniAppSecret;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(String funcInfo) {
        this.funcInfo = funcInfo;
    }

    public List<FuncInfo> getFuncInfoList() {
        return JSONUtil.readValueAsList(funcInfo, FuncInfo.class);
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

    public int getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(int serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public int getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(int verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getMiniAppSecret() {
        return miniAppSecret;
    }

    public void setMiniAppSecret(String miniAppSecret) {
        this.miniAppSecret = miniAppSecret;
    }

}
