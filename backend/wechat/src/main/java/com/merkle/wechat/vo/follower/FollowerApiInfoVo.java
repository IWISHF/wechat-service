package com.merkle.wechat.vo.follower;

import java.util.List;

import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.follower.MemberAttribute;

import io.swagger.annotations.ApiModelProperty;

public class FollowerApiInfoVo {
    private String nickname;

    @ApiModelProperty(notes = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

    @ApiModelProperty(notes = "用户的语言，简体中文为zh_CN")
    private String language;

    private String city;

    private String province;

    private String country;

    private String headimgurl;

    private FollowerBindInfo bindInfo;

    private List<MemberAttribute> attributes;

    public List<MemberAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MemberAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public FollowerBindInfo getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(FollowerBindInfo bindInfo) {
        this.bindInfo = bindInfo;
    }

}
