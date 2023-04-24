package com.merkle.wechat.service.menu;

import java.util.List;
import java.util.Set;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.menu.MenuButton;

import weixin.popular.bean.menu.Menu;
import weixin.popular.bean.menu.MenuButtons;

public interface MenuService {

    Menu getAllMenue(long channelId);

    MenuButtons getDefaultMenue(long channelId);

    List<MenuButtons> getConditionMenue(long channelId);

    void createDefaultMenu(long channelId, MenuButtons defaultButtons);

    void createAdditionalMenu(long channelId, MenuButtons conditionalButtons);

    void syncMenu(long channelId) throws Exception;

    void concealCurrentMenuButtons(Set<MenuButton> button);

    void updateMenuButton(Set<MenuButton> menuButtons, WechatPublicNo pbNo);

}
