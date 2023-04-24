package com.merkle.wechat.service.menu;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.menu.ConditionalMenuDao;
import com.merkle.wechat.common.dao.menu.DefaultMenuDao;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.menu.ConditionalMenu;
import com.merkle.wechat.common.entity.menu.DefaultMenu;
import com.merkle.wechat.common.entity.menu.Matchrule;
import com.merkle.wechat.common.entity.menu.MenuButton;
import com.merkle.wechat.common.entity.menu.MenuClickAction;
import com.merkle.wechat.common.entity.menu.MenuMiniProgramAction;
import com.merkle.wechat.common.entity.menu.MenuViewAction;
import com.merkle.wechat.common.entity.menu.SubButton;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.service.TagService;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;

import weixin.popular.api.MenuAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.menu.Button;
import weixin.popular.bean.menu.Menu;
import weixin.popular.bean.menu.MenuButtons;

@Component
public class MenuServiceImpl implements MenuService {
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;
    private @Autowired DefaultMenuDao defaultMenuDaoImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired ConditionalMenuDao conditionalMenuDaoImpl;
    private @Autowired TagService tagService;

    @Override
    public Menu getAllMenue(long channelId) {
        Optional<WechatPublicNo> pbNoOp = Optional.ofNullable(wechatPbNoServiceImpl.findOneById(channelId));
        return MenuAPI.menuGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNoOp.get().getAuthorizerAppid()));
    }

    @Override
    public MenuButtons getDefaultMenue(long channelId) {
        Optional<WechatPublicNo> pbNoOp = Optional.ofNullable(wechatPbNoServiceImpl.findOneById(channelId));
        Optional<Menu> menuOp = Optional.ofNullable(
                MenuAPI.menuGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNoOp.get().getAuthorizerAppid())));
        Menu menu = menuOp.get();
        if (menu.isSuccess()) {
            return menu.getMenu();
        } else {
            throw new ServiceWarn(menu.getErrmsg(), Integer.valueOf(menu.getErrcode()));
        }
    }

    @Override
    public List<MenuButtons> getConditionMenue(long channelId) {
        Optional<WechatPublicNo> pbNoOp = Optional.ofNullable(wechatPbNoServiceImpl.findOneById(channelId));
        Optional<Menu> menuOp = Optional.ofNullable(
                MenuAPI.menuGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNoOp.get().getAuthorizerAppid())));
        Menu menu = menuOp.get();
        if (menu.isSuccess()) {
            return menu.getConditionalmenu();
        } else {
            throw new ServiceWarn(menu.getErrmsg(), Integer.valueOf(menu.getErrcode()));
        }
    }

    @Override
    public void createDefaultMenu(long channelId, MenuButtons defaultButtons) {
        Optional<WechatPublicNo> pbNoOp = Optional.ofNullable(wechatPbNoServiceImpl.findOneById(channelId));
        BaseResult menuCreate = MenuAPI.menuCreate(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNoOp.get().getAuthorizerAppid()), defaultButtons);
        if (!menuCreate.isSuccess()) {
            throw new ServiceWarn(menuCreate.getErrmsg(), menuCreate.getErrcode());
        }
    }

    @Override
    public void createAdditionalMenu(long channelId, MenuButtons conditionalButtons) {
        Optional<WechatPublicNo> pbNoOp = Optional.ofNullable(wechatPbNoServiceImpl.findOneById(channelId));
        BaseResult menuCreate = MenuAPI.menuAddconditional(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNoOp.get().getAuthorizerAppid()), conditionalButtons);
        if (!menuCreate.isSuccess()) {
            throw new ServiceWarn(menuCreate.getErrmsg(), menuCreate.getErrcode());
        }
    }

    @Override
    public void syncMenu(long channelId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        Menu result = MenuAPI.menuGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()));
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
        syncDefaultMenu(result.getMenu(), pbNo);
        syncConditionalMenu(result.getConditionalmenu(), pbNo);

    }

    private void syncConditionalMenu(List<MenuButtons> conditionalmenus, WechatPublicNo pbNo) {
        if (conditionalmenus == null || conditionalmenus.size() == 0) {
            return;
        }
        int indexStr = 1;
        for (MenuButtons conditionalMenu : conditionalmenus) {
            syncConditionalMenu(conditionalMenu, pbNo, indexStr++);
        }
    }

    private void syncConditionalMenu(MenuButtons conditonalMenu, WechatPublicNo pbNo, int indexStr) {
        ConditionalMenu menu = convertWeixinConditionalMenuToConditionalMenu(conditonalMenu, pbNo);
        menu.setPublished(true);
        menu.setIndexStr(indexStr);
        conditionalMenuDaoImpl.save(menu);
    }

    private ConditionalMenu convertWeixinConditionalMenuToConditionalMenu(MenuButtons conditonalMenu,
            WechatPublicNo pbNo) {
        ConditionalMenu menu = new ConditionalMenu();
        menu.setMenuid(conditonalMenu.getMenuid());
        menu.setUpdatedDate(new Date());
        menu.setWechatPublicNoId(pbNo.getId());
        Set<MenuButton> menuButtons = new HashSet<>();
        Button[] weixinMenuButtons = conditonalMenu.getButton();
        int mbIndex = 1;
        for (Button weixinMenuButton : weixinMenuButtons) {
            MenuButton mb = new MenuButton();
            mb.setIndexStr(mbIndex++);
            mb.setName(weixinMenuButton.getName());
            mb.setType(weixinMenuButton.getType());
            setActionToMenuButton(mb, weixinMenuButton, pbNo);

            Set<SubButton> dbSubButtons = new HashSet<>();
            List<Button> weixinSubButtons = weixinMenuButton.getSub_button();
            int subIndexStr = 1;
            for (Button weixinSub : weixinSubButtons) {
                SubButton subButton = new SubButton();
                subButton.setIndexStr(subIndexStr++);
                subButton.setName(weixinSub.getName());
                subButton.setType(weixinSub.getType());
                setActionToSubButton(subButton, weixinSub, pbNo);
                dbSubButtons.add(subButton);
            }

            mb.setSubButton(dbSubButtons);
            menuButtons.add(mb);

        }
        menu.setButton(menuButtons);

        // Current only support for tag / group
        Matchrule rule = new Matchrule();
        Integer groupId = conditonalMenu.getMatchrule().getGroup_id();
        if (groupId != null) {
            Tag tag = tagService.getTagByWeixinTagId(groupId, pbNo.getId());
            rule.setTag(tag);
        }
        rule.setCity(conditonalMenu.getMatchrule().getCity());
        rule.setClientPlatformType(conditonalMenu.getMatchrule().getClient_platform_type());
        rule.setCountry(conditonalMenu.getMatchrule().getCountry());
        rule.setLanguage(conditonalMenu.getMatchrule().getLanguage());
        rule.setProvince(conditonalMenu.getMatchrule().getProvince());
        rule.setSex(conditonalMenu.getMatchrule().getSex());
        menu.setMatchrule(rule);

        return menu;
    }

    private void syncDefaultMenu(MenuButtons weixinDefaultMenu, WechatPublicNo pbNo) {
        DefaultMenu menu = convertWeixinDefaultMenuToDefaultMenu(weixinDefaultMenu, pbNo);
        menu.setPublished(true);
        defaultMenuDaoImpl.save(menu);
    }

    private DefaultMenu convertWeixinDefaultMenuToDefaultMenu(MenuButtons weixinDefaultMenu, WechatPublicNo pbNo) {
        DefaultMenu defaultMenu = new DefaultMenu();
        defaultMenu.setMenuid(weixinDefaultMenu.getMenuid());
        defaultMenu.setUpdatedDate(new Date());
        defaultMenu.setWechatPublicNoId(pbNo.getId());
        Set<MenuButton> menuButtons = new HashSet<>();
        Button[] weixinMenuButtons = weixinDefaultMenu.getButton();
        int mbIndex = 1;
        for (Button weixinMenuButton : weixinMenuButtons) {
            MenuButton mb = new MenuButton();
            mb.setIndexStr(mbIndex++);
            mb.setName(weixinMenuButton.getName());
            mb.setType(weixinMenuButton.getType());
            setActionToMenuButton(mb, weixinMenuButton, pbNo);

            Set<SubButton> dbSubButtons = new HashSet<>();
            List<Button> weixinSubButtons = weixinMenuButton.getSub_button();
            int subIndexStr = 1;
            for (Button weixinSub : weixinSubButtons) {
                SubButton subButton = new SubButton();
                subButton.setIndexStr(subIndexStr++);
                subButton.setName(weixinSub.getName());
                subButton.setType(weixinSub.getType());
                setActionToSubButton(subButton, weixinSub, pbNo);
                dbSubButtons.add(subButton);
            }

            mb.setSubButton(dbSubButtons);
            menuButtons.add(mb);

        }
        defaultMenu.setButton(menuButtons);
        return defaultMenu;
    }

    private void setActionToSubButton(SubButton subButton, Button weixinSub, WechatPublicNo pbNo) {
        if (weixinSub.getType() != null) {
            switch (weixinSub.getType()) {
                case "click": {
                    MenuClickAction action = new MenuClickAction();
                    action.setKeycode(weixinSub.getKey());
                    action.setToUserName(pbNo.getUserName());
                    action.setEnable(true);
                    action.setWechatPublicNoId(pbNo.getId());
                    subButton.setClickAction(action);
                    break;
                }
                case "view": {
                    MenuViewAction action = new MenuViewAction();
                    action.setUrl(weixinSub.getUrl());
                    subButton.setMenuViewAction(action);
                    break;
                }
                case "miniprogram": {
                    MenuMiniProgramAction action = new MenuMiniProgramAction();
                    action.setAppid(weixinSub.getAppid());
                    action.setMediaId(weixinSub.getMedia_id());
                    action.setPagepath(weixinSub.getPagepath());
                    action.setUrl(weixinSub.getUrl());
                    subButton.setMiniProgramAction(action);
                    break;
                }
            }
        }

    }

    private void setActionToMenuButton(MenuButton mb, Button weixinMenuButton, WechatPublicNo pbNo) {
        if (weixinMenuButton.getType() != null) {

            switch (weixinMenuButton.getType()) {
                case "click": {
                    MenuClickAction action = new MenuClickAction();
                    action.setKeycode(weixinMenuButton.getKey());
                    action.setToUserName(pbNo.getUserName());
                    action.setEnable(true);
                    action.setWechatPublicNoId(pbNo.getId());
                    mb.setClickAction(action);
                    break;
                }
                case "view": {
                    MenuViewAction action = new MenuViewAction();
                    action.setUrl(weixinMenuButton.getUrl());
                    mb.setMenuViewAction(action);
                    break;
                }
                case "miniprogram": {
                    MenuMiniProgramAction action = new MenuMiniProgramAction();
                    action.setAppid(weixinMenuButton.getAppid());
                    action.setMediaId(weixinMenuButton.getMedia_id());
                    action.setPagepath(weixinMenuButton.getPagepath());
                    action.setUrl(weixinMenuButton.getUrl());
                    mb.setMiniProgramAction(action);
                    break;
                }
            }
        }
    }

    @Override
    public void concealCurrentMenuButtons(Set<MenuButton> button) {
        if (button == null || button.size() == 0) {
            return;
        }
        button.forEach((b) -> {
            concealCurrentMenuButton(b);
        });
    }

    private void concealCurrentMenuButton(MenuButton b) {
        MenuClickAction clickAction = b.getClickAction();
        concealClickAction(clickAction);
        concealSubButtons(b.getSubButton());
    }

    private void concealSubButtons(Set<SubButton> subButton) {
        if (subButton == null || subButton.size() == 0) {
            return;
        }
        subButton.forEach((sb) -> {
            concealSubButton(sb);
        });
    }

    private void concealSubButton(SubButton sb) {
        concealClickAction(sb.getClickAction());
    }

    private void concealClickAction(MenuClickAction clickAction) {
        if (clickAction == null) {
            return;
        }
        clickAction.setEnable(false);
    }

    @Override
    public void updateMenuButton(Set<MenuButton> menuButtons, WechatPublicNo pbNo) {
        if (menuButtons != null) {

            menuButtons.forEach((mb) -> {
                if (mb.getSubButton() != null) {
                    mb.getSubButton().forEach((sb) -> {
                        if (sb.getClickAction() != null) {
                            sb.getClickAction().setToUserName(pbNo.getUserName());
                            sb.getClickAction().setWechatPublicNoId(pbNo.getId());
                            if (StringUtils.isEmpty(sb.getClickAction().getKeycode())) {
                                sb.getClickAction().setKeycode(generateKeyCode(pbNo));
                            }
                            sb.getClickAction().setEnable(true);
                        }

                    });
                }
                if (mb.getClickAction() != null) {
                    mb.getClickAction().setToUserName(pbNo.getUserName());
                    if (StringUtils.isEmpty(mb.getClickAction().getKeycode())) {
                        mb.getClickAction().setKeycode(generateKeyCode(pbNo));
                    }
                    mb.getClickAction().setEnable(true);
                    mb.getClickAction().setWechatPublicNoId(pbNo.getId());
                }
            });
        }
    }

    private String generateKeyCode(WechatPublicNo pbNo) {
        return "key_d" + pbNo.getId() + "_" + UUID.randomUUID();
    }

}
