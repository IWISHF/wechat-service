package com.merkle.wechat.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.follower.Follower;

@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @SafeHtml
    private String name;

    // 微信标签对应的Id
    private Long tagId;

    // 此标签下的粉丝数量
    private Long count = 0L;

    private boolean fromWechat = false;

    @JoinColumn(name = "groupId")
    @ManyToOne(fetch = FetchType.EAGER)
    private TagGroup group;

    private Long wechatPublicNoId;

    @JsonIgnore
    @ManyToMany(mappedBy = "newSubscribeTags")
    private Set<Qrcode> newSubscribeTagOrcodes = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "alreadySubscribeTags")
    private Set<Qrcode> alreadySubscribeTagQrcodes = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Follower> tagFollowers = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<TagGroupCondition> tagGroupConditions = new HashSet<>();

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isFromWechat() {
        return fromWechat;
    }

    public void setFromWechat(boolean fromWechat) {
        this.fromWechat = fromWechat;
    }

    public TagGroup getGroup() {
        return group;
    }

    public void setGroup(TagGroup group) {
        this.group = group;
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

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public Set<Qrcode> getNewSubscribeTagOrcodes() {
        return newSubscribeTagOrcodes;
    }

    public void setNewSubscribeTagOrcodes(Set<Qrcode> newSubscribeTagOrcodes) {
        this.newSubscribeTagOrcodes = newSubscribeTagOrcodes;
    }

    public Set<Qrcode> getAlreadySubscribeTagQrcodes() {
        return alreadySubscribeTagQrcodes;
    }

    public void setAlreadySubscribeTagQrcodes(Set<Qrcode> alreadySubscribeTagQrcodes) {
        this.alreadySubscribeTagQrcodes = alreadySubscribeTagQrcodes;
    }

    public Set<Follower> getTagFollowers() {
        return tagFollowers;
    }

    public void setTagFollowers(Set<Follower> tagFollowers) {
        this.tagFollowers = tagFollowers;
    }

    public Set<TagGroupCondition> getTagGroupConditions() {
        return tagGroupConditions;
    }

    public void setTagGroupConditions(Set<TagGroupCondition> tagGroupConditions) {
        this.tagGroupConditions = tagGroupConditions;
    }

}
