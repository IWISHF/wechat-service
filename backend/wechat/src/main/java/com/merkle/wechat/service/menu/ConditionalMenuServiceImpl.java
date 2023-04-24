package com.merkle.wechat.service.menu;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.menu.ConditionalMenuDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.menu.ConditionalMenu;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.menu.ConditionalMenuVo;
import com.merkle.wechat.vo.menu.converter.MenuConverter;

import weixin.popular.api.MenuAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.menu.MenuAddconditionalResult;
import weixin.popular.bean.menu.MenuButtons;

@Component
public class ConditionalMenuServiceImpl implements ConditonalMenuService {
    private @Autowired ConditionalMenuDao conditionalMenuDaoImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired MenuConverter menuConverter;
    private @Autowired MenuService menuServiceImpl;

    @Override
    public List<ConditionalMenuVo> getActiveConditionalMenu(Long channelId) throws Exception {
        pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        List<ConditionalMenu> menus = conditionalMenuDaoImpl
                .findByWechatPublicNoIdAndPublishedOrderByIndexStrDesc(channelId, true);
        return menuConverter.convertConditionalMenusToVos(menus, channelId);
    }

    @Override
    public ConditionalMenuVo getActiveConditionalMenu(Long channelId, Long id) {
        ConditionalMenu menu = Optional
                .ofNullable(conditionalMenuDaoImpl.findByWechatPublicNoIdAndPublishedAndId(channelId, true, id))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        return menuConverter.convertConditionalMenuToVo(menu, channelId);
    }

    @Override
    public void publishConditionalMenu(Long pbNoId, ConditionalMenu conditionalMenu) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(pbNoId);
        int indexStr = conditionalMenuDaoImpl.countByWechatPublicNoId(pbNoId) + 1;
        conditionalMenu.setWechatPublicNoId(pbNo.getId());
        conditionalMenu.setIndexStr(indexStr);
        menuServiceImpl.updateMenuButton(conditionalMenu.getButton(), pbNo);
        conditionalMenu.setPublished(true);
        String menuid = publishConditionalMenu(conditionalMenu, pbNo);
        conditionalMenu.setMenuid(menuid);
        conditionalMenuDaoImpl.save(conditionalMenu);
    }

    private String publishConditionalMenu(ConditionalMenu conditionalMenu, WechatPublicNo pbNo) {
        MenuButtons weixinConditionalMenu = menuConverter.convertConditionalMenuToWeixinMenu(conditionalMenu);
         MenuAddconditionalResult menuCreate = MenuAPI.menuAddconditional(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), weixinConditionalMenu);
        if (!menuCreate.isSuccess()) {
            throw new ServiceWarn(menuCreate.getErrmsg(), menuCreate.getErrcode());
        }

        return menuCreate.getMenuid();
    }

    @Override
    public void delConditionalMenu(Long channelId, Long id) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        ConditionalMenu menu = Optional
                .ofNullable(conditionalMenuDaoImpl.findOneByWechatPublicNoIdAndIdAndPublished(pbNo.getId(), id, true))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));

        String menuid = menu.getMenuid();
        BaseResult result = MenuAPI
                .menuDelconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), menuid);
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
        concealConditonalMenu(menu);
        menu.getMatchrule().setTag(null); 
        conditionalMenuDaoImpl.save(menu);
    }

    private void concealConditonalMenu(ConditionalMenu menu) {
        menu.setPublished(false);
        menuServiceImpl.concealCurrentMenuButtons(menu.getButton());
    }

}
