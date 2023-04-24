package com.merkle.wechat.vo.tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.vo.batch.BatchTaskVo;
import com.merkle.wechat.vo.menu.ConditionalMenuVo;
import com.merkle.wechat.vo.qrcode.QrcodeVo;

public class TagVo {
    private Long id;

    private String name;

    // 微信标签对应的Id
    private Long tagId;

    // 此标签下的粉丝数量
    private Long count;

    private boolean fromWechat = false;

    private TagGroup group;

    private Long wechatPublicNoId;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    private List<QrcodeVo> qrcodes = new ArrayList<>();

    private List<BatchTaskVo> batchJobs = new ArrayList<>();

    private List<ConditionalMenuVo> menus = new ArrayList<>();

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

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
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

    public List<QrcodeVo> getQrcodes() {
        return qrcodes;
    }

    public void setQrcodes(List<QrcodeVo> qrcodes) {
        this.qrcodes = qrcodes;
    }

    public List<BatchTaskVo> getBatchJobs() {
        return batchJobs;
    }

    public void setBatchJobs(List<BatchTaskVo> batchJobs) {
        this.batchJobs = batchJobs;
    }

    public List<ConditionalMenuVo> getMenus() {
        return menus;
    }

    public void setMenus(List<ConditionalMenuVo> menus) {
        this.menus = menus;
    }

}
