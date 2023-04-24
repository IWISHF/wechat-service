package com.merkle.wechat.modules.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.demo.service.DemoMenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import weixin.popular.bean.menu.Menu;
import weixin.popular.bean.menu.TrymatchResult;

@Controller
@RequestMapping("/wechat/demo")
@Api(tags = "DemoMenu")
public class DemoMenuController extends AbstractController {

    private @Autowired DemoMenuService menuServiceImpl;
    
    @NeedWrap
    @RequestMapping("/oauth/menu")
    @ApiOperation("menue get")
    public Menu getMenue(String appid) throws Exception {
        return menuServiceImpl.getMenue(appid);
    }
    
    @NeedWrap
    @RequestMapping("/oauth/menu/create/nangjing")
    @ApiOperation("menu nanjing")
    public void createNanjing() throws Exception {
        menuServiceImpl.createNanjingMenue();
    }
    
    @NeedWrap
    @RequestMapping("/oauth/menu/create/merkle")
    @ApiOperation("menu merkle")
    public void createMerkle() throws Exception {
        menuServiceImpl.createMerkleMenue();
    }
    
    @NeedWrap
    @RequestMapping("/oauth/menu/restore/nanjing")
    @ApiOperation("restore nanjing")
    public void retoreNanjingMenu() throws Exception {
        menuServiceImpl.restoreNanjingDefaultMenu();
    }
    
    @NeedWrap
    @RequestMapping("/oauth/menu/restore/merkle")
    @ApiOperation("restore merkle")
    public void restorMerkleMenu() throws Exception {
        menuServiceImpl.restoreMerkleDefaultMenu();
    }
    
    @NeedWrap
    @RequestMapping("/oauth/menu/trymatch")
    public TrymatchResult tagFollower(String openid) throws Exception {
        return menuServiceImpl.tagTryMatchMenu(openid, "wx50f190ca3a0ee011");
    }
    
    @NeedWrap
    @PostMapping("/menu/{channelId}/default/create")
    @ApiOperation("restore merkle default")
    public void createDefaultMenu(String menuJson, @PathVariable long channelId) throws Exception {
        menuServiceImpl.restoreMerkleDefaultMenu();
    }
    
    @NeedWrap
    @RequestMapping("/menu")
    public Menu getDefaultMenue(String appid) throws Exception {
        return menuServiceImpl.getMenue(appid);
    }
    
    @NeedWrap
    @GetMapping("/menu/delete")
    public void delMenue(String menuId, String appId) throws Exception {
        menuServiceImpl.delMenue(menuId, appId);
    }

}
