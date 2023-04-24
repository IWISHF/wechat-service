package com.merkle.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.menu.DefaultMenu;
import com.merkle.wechat.service.menu.DefaultMenuService;
import com.merkle.wechat.vo.menu.DefaultMenuVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = { "默认菜单管理" })
@RequestMapping("/wechat/{channelId}/management/menu/default")
public class DefaultMenuMannagementController extends AbstractController {

    private @Autowired DefaultMenuService defaultMenuServiceImpl;

    @NeedWrap
    @GetMapping
    @ApiOperation("获取默认菜单")
    public DefaultMenuVo getDefaultMenu(@PathVariable Long channelId) throws Exception {
        return defaultMenuServiceImpl.getDefaultMenu(channelId);
    }

    @NeedWrap
    @PostMapping
    @ApiOperation("发布默认菜单")
    public String publishDefaultMenu(@PathVariable Long channelId, @RequestBody DefaultMenu defaultMenu)
            throws Exception {
        defaultMenuServiceImpl.publishDefaultMenu(channelId, defaultMenu);
        return "ok";
    }

}
