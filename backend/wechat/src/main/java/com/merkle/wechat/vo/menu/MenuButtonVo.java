package com.merkle.wechat.vo.menu;

import java.util.List;

import com.merkle.wechat.common.entity.menu.MenuMiniProgramAction;
import com.merkle.wechat.common.entity.menu.MenuViewAction;

import io.swagger.annotations.ApiModelProperty;

public class MenuButtonVo {
    // private Long id;

    @ApiModelProperty(allowableValues = "click, view")
    private String type; // 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型

    private String name; // 菜单标题，不超过16个字节

    private int indexStr;

    private MenuClickActionVo clickAction;

    private MenuMiniProgramAction miniProgramAction;

    private MenuViewAction menuViewAction;

    private List<SubButtonVo> subButton;

    // public Long getId() {
    // return id;
    // }
    //
    // public void setId(Long id) {
    // this.id = id;
    // }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
    }

    public MenuClickActionVo getClickAction() {
        return clickAction;
    }

    public void setClickAction(MenuClickActionVo clickAction) {
        this.clickAction = clickAction;
    }

    public MenuMiniProgramAction getMiniProgramAction() {
        return miniProgramAction;
    }

    public void setMiniProgramAction(MenuMiniProgramAction miniProgramAction) {
        this.miniProgramAction = miniProgramAction;
    }

    public MenuViewAction getMenuViewAction() {
        return menuViewAction;
    }

    public void setMenuViewAction(MenuViewAction menuViewAction) {
        this.menuViewAction = menuViewAction;
    }

    public List<SubButtonVo> getSubButton() {
        return subButton;
    }

    public void setSubButton(List<SubButtonVo> subButton) {
        this.subButton = subButton;
    }

}
