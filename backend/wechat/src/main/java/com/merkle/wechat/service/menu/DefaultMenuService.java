package com.merkle.wechat.service.menu;

import com.merkle.wechat.common.entity.menu.DefaultMenu;
import com.merkle.wechat.vo.menu.DefaultMenuVo;

public interface DefaultMenuService {

    DefaultMenuVo getDefaultMenu(Long channelId) throws Exception;

    void publishDefaultMenu(Long channelId, DefaultMenu defaultMenu) throws Exception;

}
