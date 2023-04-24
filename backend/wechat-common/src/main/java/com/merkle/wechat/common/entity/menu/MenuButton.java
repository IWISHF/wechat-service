package com.merkle.wechat.common.entity.menu;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "menu_button")
public class MenuButton extends AbastractButton {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "创建不必须")
    private Long id;

    @ApiModelProperty(allowableValues = "click,view,miniprogram")
    private String type; // 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型

    @Length(min = 1, max = 16)
    @ApiModelProperty(notes = "length [1,16]")
    private String name; // 菜单标题，不超过16个字节

    private int indexStr;

    @JoinColumn(name = "click_action_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MenuClickAction clickAction;

    @JoinColumn(name = "mini_program_action_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MenuMiniProgramAction miniProgramAction;

    @JoinColumn(name = "menu_view_action_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MenuViewAction menuViewAction;

    @Size(max = 5)
    @OrderBy("indexStr asc")
    @JoinColumn(name = "menu_button_id")
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<SubButton> subButton;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
