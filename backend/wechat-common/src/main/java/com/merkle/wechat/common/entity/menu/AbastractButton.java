package com.merkle.wechat.common.entity.menu;

import java.util.Set;

public class AbastractButton {
    private String type; // 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型

    private String name; // 菜单标题，不超过16个字节

    private int indexStr;

    private MenuClickAction clickAction;

    private MenuMiniProgramAction miniProgramAction;

    private MenuViewAction menuViewAction;

    private Set<SubButton> subButton;

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

    public MenuClickAction getClickAction() {
        return clickAction;
    }

    public void setClickAction(MenuClickAction clickAction) {
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

    public Set<SubButton> getSubButton() {
        return subButton;
    }

    public void setSubButton(Set<SubButton> subButton) {
        this.subButton = subButton;
    }

}
