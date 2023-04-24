package com.merkle.wechat.vo.follower;

import java.util.HashSet;
import java.util.Set;

import com.merkle.wechat.common.entity.Tag;

import io.swagger.annotations.ApiModelProperty;

public class FollowerVo {
    private Long id;

    @ApiModelProperty(notes = "值为0时，代表此用户没有关注该公众号, 1代表关注")
    private Integer subscribe; // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。

    private String openid;

    private String nickname;

    private String nicknameEmoji;

    @ApiModelProperty(notes = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

    @ApiModelProperty(notes = "用户的语言，简体中文为zh_CN")
    private String language;

    private String city;

    private String province;

    private String country;

    private String headimgurl;

    @ApiModelProperty(notes = "时间戳 秒")
    private Integer subscribeTime;

    @ApiModelProperty(notes = "标签")
    private Set<Tag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNicknameEmoji() {
        return nicknameEmoji;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
