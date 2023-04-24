package com.merkle.wechat.common.entity.menu;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.merkle.wechat.common.entity.AutoReplyRule;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "menu_click_action")
public class MenuClickAction {
    @Id
    @ApiModelProperty(notes = "创建不必须")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(notes = "创建不必须")
    private String keycode; // click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节

    @JoinColumn(name = "auto_reply_rule_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AutoReplyRule rule;

    @ApiModelProperty(notes = "创建不必须")
    private String toUserName;

    @ApiModelProperty(notes = "创建不必须")
    private Long wechatPublicNoId;

    @ApiModelProperty(notes = "创建不必须")
    private boolean enable = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public AutoReplyRule getRule() {
        return rule;
    }

    public void setRule(AutoReplyRule rule) {
        this.rule = rule;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

}
