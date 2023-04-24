package com.merkle.wechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.service.menu.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import weixin.popular.bean.menu.Menu;
import weixin.popular.bean.menu.MenuButtons;

@Controller
@RequestMapping("/wechat/{channelId}/management/menu/deprecated")
@Deprecated
@Api(tags = "过时menu管理", hidden = true)
@ApiIgnore
public class MenuManagementController extends AbstractController {

    private @Autowired MenuService menuServiceImpl;

    @NeedWrap
    @GetMapping("/all")
    public Menu getAllMenu(@PathVariable long channelId) throws Exception {
        return menuServiceImpl.getAllMenue(channelId);
    }

    @NeedWrap
    @GetMapping("/default")
    public MenuButtons getDefaultMenu(@PathVariable long channelId) throws Exception {
        return menuServiceImpl.getDefaultMenue(channelId);
    }

    @NeedWrap
    @GetMapping("/conditional")
    public List<MenuButtons> getConditionMenus(@PathVariable long channelId) throws Exception {
        return menuServiceImpl.getConditionMenue(channelId);
    }

    @NeedWrap
    @PostMapping("/conditional")
    public String createConditionMenus(@PathVariable long channelId, @RequestBody MenuButtons conditionalButtons)
            throws Exception {
        menuServiceImpl.createAdditionalMenu(channelId, conditionalButtons);
        return "ok";
    }

    @NeedWrap
    @PostMapping("/default")
    public String createDefaultMenu(@PathVariable long channelId, @RequestBody MenuButtons defaultMenu)
            throws Exception {
        menuServiceImpl.createDefaultMenu(channelId, defaultMenu);
        return "success";
    }

    @NeedWrap
    @GetMapping("/all/sync")
    @ApiOperation(hidden = true, value = "sync all menu")
    public String syncAllMenu(@PathVariable long channelId) throws Exception {
        menuServiceImpl.syncMenu(channelId);
        return "ok";
    }
}
