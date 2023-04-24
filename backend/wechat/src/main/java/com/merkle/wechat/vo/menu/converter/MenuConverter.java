package com.merkle.wechat.vo.menu.converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.menu.AbastractButton;
import com.merkle.wechat.common.entity.menu.ConditionalMenu;
import com.merkle.wechat.common.entity.menu.DefaultMenu;
import com.merkle.wechat.common.entity.menu.Matchrule;
import com.merkle.wechat.common.entity.menu.MenuButton;
import com.merkle.wechat.common.entity.menu.MenuClickAction;
import com.merkle.wechat.common.entity.menu.SubButton;
import com.merkle.wechat.vo.autoreply.converter.AutoReplyRuleConverter;
import com.merkle.wechat.vo.menu.ConditionalMenuVo;
import com.merkle.wechat.vo.menu.DefaultMenuVo;
import com.merkle.wechat.vo.menu.MatchruleVo;
import com.merkle.wechat.vo.menu.MenuButtonVo;
import com.merkle.wechat.vo.menu.MenuClickActionVo;
import com.merkle.wechat.vo.menu.SubButtonVo;

import weixin.popular.bean.menu.Button;
import weixin.popular.bean.menu.MenuButtons;

@Component
public class MenuConverter {
    private @Autowired AutoReplyRuleConverter autoReplyRuleConverter;

    public List<ConditionalMenuVo> convertConditionalMenusToVos(List<ConditionalMenu> menus, Long pbNoId) {
        if (menus == null || menus.size() == 0) {
            return new ArrayList<ConditionalMenuVo>();
        }
        List<ConditionalMenuVo> vos = new ArrayList<>();
        menus.forEach((cm) -> {
            vos.add(convertConditionalMenuToVo(cm, pbNoId));
        });
        return vos;
    }

    public ConditionalMenuVo convertConditionalMenuToVo(ConditionalMenu cm, Long pbNoId) {
        ConditionalMenuVo vo = new ConditionalMenuVo();
        BeanUtils.copyProperties(cm, vo, "button", "matchrule");
        vo.setMatchrule(convertMatchruleToVo(cm.getMatchrule()));
        vo.setButton(convertMenuButtonsToVos(cm.getButton(), pbNoId));
        return vo;
    }

    private MatchruleVo convertMatchruleToVo(Matchrule matchrule) {
        if (matchrule == null) {
            return null;
        }
        MatchruleVo vo = new MatchruleVo();
        vo.setTag(matchrule.getTag());
        return vo;
    }

    public DefaultMenuVo convertDefaultMenuToVo(DefaultMenu dbMenu, Long pbNoId) {
        DefaultMenuVo vo = new DefaultMenuVo();
        BeanUtils.copyProperties(dbMenu, vo, "button");
        vo.setButton(convertMenuButtonsToVos(dbMenu.getButton(), pbNoId));
        return vo;
    }

    private List<MenuButtonVo> convertMenuButtonsToVos(Set<MenuButton> button, Long pbNoId) {
        List<MenuButtonVo> vos = new ArrayList<MenuButtonVo>();
        if (button == null || button.size() == 0) {
            return vos;
        }
        button.forEach((b) -> {
            vos.add(convertMenuButtonToVo(b, pbNoId));
        });
        return vos;
    }

    private MenuButtonVo convertMenuButtonToVo(MenuButton button, Long pbNoId) {
        MenuButtonVo vo = new MenuButtonVo();
        BeanUtils.copyProperties(button, vo, "clickAction", "subButton");
        vo.setClickAction(convertClickActionToVo(button.getClickAction(), pbNoId));
        vo.setSubButton(convertSubButtonsToVos(button.getSubButton(), pbNoId));
        return vo;
    }

    private List<SubButtonVo> convertSubButtonsToVos(Set<SubButton> subButton, Long pbNoId) {
        List<SubButtonVo> vos = new ArrayList<>();
        if (subButton == null || subButton.size() == 0) {
            return vos;
        }
        subButton.forEach((sb) -> {
            vos.add(convertSubButtonToVo(sb, pbNoId));
        });
        return vos;
    }

    private SubButtonVo convertSubButtonToVo(SubButton sb, Long pbNoId) {
        SubButtonVo vo = new SubButtonVo();
        BeanUtils.copyProperties(sb, vo, "clickAction");
        vo.setClickAction(convertClickActionToVo(sb.getClickAction(), pbNoId));
        return vo;
    }

    private MenuClickActionVo convertClickActionToVo(MenuClickAction clickAction, Long pbNoId) {
        if (clickAction == null) {
            return null;
        }
        MenuClickActionVo vo = new MenuClickActionVo();
        BeanUtils.copyProperties(clickAction, vo, "rule");
        if (clickAction.getRule() != null) {
            vo.setRule(autoReplyRuleConverter.convertAutoReplyRule(clickAction.getRule(), pbNoId));
        }
        return vo;
    }

    public MenuButtons convertDefaultMenuToWeixinMenu(DefaultMenu defaultMenu) {
        MenuButtons weixinMenu = new MenuButtons();
        weixinMenu.setButton(convertMenuButtonsToWeixinButtons(defaultMenu.getButton()));
        return weixinMenu;
    }

    private Button[] convertMenuButtonsToWeixinButtons(Set<MenuButton> button) {
        if (button == null || button.size() == 0) {
            return null;
        }
        List<MenuButton> sortedButton = button.stream().sorted(new Comparator<MenuButton>() {

            @Override
            public int compare(MenuButton o1, MenuButton o2) {
                if (o1.getIndexStr() > o2.getIndexStr()) {
                    return 1;
                } else if (o1.getIndexStr() < o2.getIndexStr()) {
                    return -1;
                }
                return 0;
            }
        }).collect(Collectors.toList());
        List<Button> weixinButtons = new ArrayList<>();
        sortedButton.forEach((mb) -> {
            Button weixinButton = convertButtonToWexinButton(mb);
            weixinButton.setSub_button(convertSubButtonsToWeixinSubButtons(mb.getSubButton()));
            weixinButtons.add(weixinButton);
        });

        return weixinButtons.toArray(new Button[weixinButtons.size()]);
    }

    private List<Button> convertSubButtonsToWeixinSubButtons(Set<SubButton> subButtons) {
        if (subButtons == null || subButtons.size() == 0) {
            return null;
        }
        List<SubButton> sortedButtons = subButtons.stream().sorted(new Comparator<SubButton>() {

            @Override
            public int compare(SubButton o1, SubButton o2) {
                if (o1.getIndexStr() > o2.getIndexStr()) {
                    return 1;
                } else if (o1.getIndexStr() < o2.getIndexStr()) {
                    return -1;
                }
                return 0;
            }
        }).collect(Collectors.toList());
        List<Button> weixinSubButtons = new ArrayList<>();
        sortedButtons.forEach((sb) -> {
            weixinSubButtons.add(convertButtonToWexinButton(sb));
        });
        return weixinSubButtons;
    }

    private Button convertButtonToWexinButton(AbastractButton button) {
        Button weixinButton = new Button();
        weixinButton.setName(button.getName());
        weixinButton.setType(button.getType());
        if (button.getType() != null) {
            switch (button.getType()) {
                case "click": {
                    if (button.getClickAction() != null) {
                        weixinButton.setKey(button.getClickAction().getKeycode());
                    }
                    break;
                }
                case "view": {
                    if (button.getMenuViewAction() != null) {
                        weixinButton.setUrl(button.getMenuViewAction().getUrl());
                    }
                    break;
                }
                case "miniprogram": {
                    if (button.getMiniProgramAction() != null) {
                        weixinButton.setAppid(button.getMiniProgramAction().getAppid());
                        weixinButton.setMedia_id(button.getMiniProgramAction().getMediaId());
                        weixinButton.setPagepath(button.getMiniProgramAction().getPagepath());
                        weixinButton.setUrl(button.getMiniProgramAction().getUrl());
                    }
                    break;
                }
            }
        }
        return weixinButton;
    }

    public MenuButtons convertConditionalMenuToWeixinMenu(ConditionalMenu conditionalMenu) {
        MenuButtons weixinMenu = new MenuButtons();
        weixinMenu.setButton(convertMenuButtonsToWeixinButtons(conditionalMenu.getButton()));
        weixinMenu.setMatchrule(convertMatchruleToWeixinMatchrule(conditionalMenu.getMatchrule()));
        return weixinMenu;
    }

    private weixin.popular.bean.menu.Matchrule convertMatchruleToWeixinMatchrule(Matchrule matchrule) {
        weixin.popular.bean.menu.Matchrule weixinrule = new weixin.popular.bean.menu.Matchrule();
        if (matchrule != null && matchrule.getTag() != null)
            weixinrule.setTag_id(matchrule.getTag().getTagId() + "");
        return weixinrule;
    }

}
