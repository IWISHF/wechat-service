package com.merkle.wechat.vo.menu;

import com.merkle.wechat.vo.autoreply.AutoReplyRuleVo;

public class MenuClickActionVo {
    // private Long id;

    private String keycode; // click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节

    @SuppressWarnings("rawtypes")
    private AutoReplyRuleVo rule;

    private String toUserName;

    // public Long getId() {
    // return id;
    // }
    //
    // public void setId(Long id) {
    // this.id = id;
    // }

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    @SuppressWarnings("rawtypes")
    public AutoReplyRuleVo getRule() {
        return rule;
    }

    @SuppressWarnings("rawtypes")
    public void setRule(AutoReplyRuleVo rule) {
        this.rule = rule;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

}
