package com.merkle.wechat.vo.follower;

import java.util.Set;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Range;

import com.merkle.wechat.common.entity.TagGroupCondition;

import io.swagger.annotations.ApiModelProperty;

public class FollowerSearchVo {
    @ApiModelProperty(allowableValues = "0,1", notes = "用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息")
    @Range(min = -1, max = 1)
    private int subscribe = -1;
    @ApiModelProperty(notes = "yyyy-MM-dd HH:mm:ss")
    private String startDate;
    @ApiModelProperty(notes = "yyyy-MM-dd HH:mm:ss")
    private String endDate;
    @Valid
    private Set<TagGroupCondition> tagGroupConditions;
    @Range(min = -1, max = 2)
    @ApiModelProperty(allowableValues = "0,1,2", notes = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private int sex = -1;

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Set<TagGroupCondition> getTagGroupConditions() {
        return tagGroupConditions;
    }

    public void setTagGroupConditions(Set<TagGroupCondition> tagGroupConditions) {
        this.tagGroupConditions = tagGroupConditions;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
