package com.merkle.wechat.modules.demo.service;

import weixin.popular.bean.menu.Menu;
import weixin.popular.bean.menu.TrymatchResult;

public interface DemoMenuService {

    Menu getMenue(String appid);

    void createNanjingMenue();

    void createMerkleMenue();

    void restoreNanjingDefaultMenu();

    void restoreMerkleDefaultMenu();

    TrymatchResult tagTryMatchMenu(String openid, String string);

    void delMenue(String menuId, String appId);

}
