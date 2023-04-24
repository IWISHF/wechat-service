package com.merkle.wechat.common.entity.follower;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Base64Utils;

import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.util.JSONUtil;

@Entity(name = "follower")
public class Follower implements Persistable<Long> {
    @Transient
    private static final long serialVersionUID = 6132950961885719097L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer subscribe; // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。

    private String openid; // 用户的标识，对当前公众号唯一

    private String nickname;

    private String nicknameEmoji; // 昵称 表情转义

    private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

    private String language;

    private String city;

    private String province;

    private String country;

    private String headimgurl;

    private Integer subscribeTime;

    @Transient
    private String[] privilege;

    private String privilegeStr; // sns 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）

    private String unionid; // 多个公众号之间用户帐号互通UnionID机制

    private Integer groupid;

    private String remark; // 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注

    @Transient
    private String[] tagidList;

    private String tagidListStr; // 用户被打上的标签ID列表

    private String subscribeScene; // 2.8.20 返回用户关注的渠道来源
                                   // ADD_SCENE_SEARCH 公众号搜索
                                   // ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移
                                   // ADD_SCENE_PROFILE_CARD 名片分享
                                   // ADD_SCENE_QR_CODE 扫描二维码
                                   // ADD_SCENEPROFILE LINK 图文页内名称点击
                                   // ADD_SCENE_PROFILE_ITEM 图文页右上角菜单
                                   // ADD_SCENE_PAID 支付后关注
                                   // ADD_SCENE_OTHERS 其他

    private Integer qrScene; // 2.8.20 二维码扫码场景（开发者自定义）

    private String qrSceneStr; // 2.8.20 二维码扫码场景描述（开发者自定义）

    private String pubNoAppId;

    private boolean recordToLoyaltySuccess;

    private String sessionKey;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "follower_tag_mapping", joinColumns = @JoinColumn(name = "followerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tagId", referencedColumnName = "id"))
    @OrderBy("id asc")
    private Set<Tag> tags = new HashSet<>();

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isRecordToLoyaltySuccess() {
        return recordToLoyaltySuccess;
    }

    public void setRecordToLoyaltySuccess(boolean recordToLoyaltySuccess) {
        this.recordToLoyaltySuccess = recordToLoyaltySuccess;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPubNoAppId() {
        return pubNoAppId;
    }

    public void setPubNoAppId(String pubNoAppId) {
        this.pubNoAppId = pubNoAppId;
    }

    public String getPrivilegeStr() {
        return privilegeStr;
    }

    public void setPrivilegeStr(String privilegeStr) {
        this.privilegeStr = privilegeStr;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return new String(Base64Utils.decodeFromString(nickname));
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNicknameEmoji() {
        return new String(Base64Utils.decodeFromString(nicknameEmoji));
    }

    public void setNicknameEmoji(String nicknameEmoji) {
        this.nicknameEmoji = nicknameEmoji;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Integer subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object[] getTagidList() {
        return JSONUtil.readValueAsList(tagidListStr, String.class).toArray();
    }

    public String getTagidListStr() {
        return tagidListStr;
    }

    public void setTagidListStr(String tagidListStr) {
        this.tagidListStr = tagidListStr;
    }

    public String getSubscribeScene() {
        return subscribeScene;
    }

    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
    }

    public Integer getQrScene() {
        return qrScene;
    }

    public void setQrScene(Integer qrScene) {
        this.qrScene = qrScene;
    }

    public String getQrSceneStr() {
        return qrSceneStr;
    }

    public void setQrSceneStr(String qrSceneStr) {
        this.qrSceneStr = qrSceneStr;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setTagidList(String[] tagidList) {
        this.tagidList = tagidList;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
