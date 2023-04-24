package com.merkle.wechat.vo.menu;

import java.util.List;

public class ConditionalMenuVo {
    private Long id;

    private String name;

    private int indexStr;

    private List<MenuButtonVo> button;

    private MatchruleVo matchrule;

    public List<MenuButtonVo> getButton() {
        return button;
    }

    public void setButton(List<MenuButtonVo> button) {
        this.button = button;
    }

    public MatchruleVo getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(MatchruleVo matchrule) {
        this.matchrule = matchrule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
