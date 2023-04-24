package com.merkle.wechat.service.menu;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.menu.DefaultMenuDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.menu.DefaultMenu;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.menu.DefaultMenuVo;
import com.merkle.wechat.vo.menu.converter.MenuConverter;

import weixin.popular.api.MenuAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.menu.MenuButtons;

@Component
public class DefaultMenuServiceImpl implements DefaultMenuService {
    private @Autowired DefaultMenuDao defaultMenuDaoImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired MenuConverter menuConverter;
    private @Autowired MenuService menuServiceImpl;

    @Override
    public DefaultMenuVo getDefaultMenu(Long channelId) throws Exception {
        pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        DefaultMenu dbMenu = Optional.ofNullable(defaultMenuDaoImpl.findByWechatPublicNoIdAndPublished(channelId, true))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        return menuConverter.convertDefaultMenuToVo(dbMenu, channelId);
    }

    @Override
    public void publishDefaultMenu(Long channelId, DefaultMenu defaultMenu) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        defaultMenu.setWechatPublicNoId(pbNo.getId());
        menuServiceImpl.updateMenuButton(defaultMenu.getButton(), pbNo);
        defaultMenu.setPublished(true);
        publishDefaultMenu(defaultMenu, pbNo);

        DefaultMenu dbMenu = defaultMenuDaoImpl.findByWechatPublicNoIdAndPublished(channelId, true);
        if (dbMenu != null) {
            concealCurrentDefaultMenu(dbMenu);
        }
        defaultMenuDaoImpl.save(defaultMenu);
    }

    private void publishDefaultMenu(DefaultMenu defaultMenu, WechatPublicNo pbNo) {
        MenuButtons weixinMenu = menuConverter.convertDefaultMenuToWeixinMenu(defaultMenu);
        BaseResult menuCreate = MenuAPI
                .menuCreate(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), weixinMenu);
        if (!menuCreate.isSuccess()) {
            throw new ServiceWarn(menuCreate.getErrmsg(), menuCreate.getErrcode());
        }
    }

    private void concealCurrentDefaultMenu(DefaultMenu dbMenu) {
        dbMenu.setPublished(false);
        menuServiceImpl.concealCurrentMenuButtons(dbMenu.getButton());
    }



}
