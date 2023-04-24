package com.merkle.wechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.menu.ConditionalMenu;
import com.merkle.wechat.service.menu.ConditonalMenuService;
import com.merkle.wechat.vo.menu.ConditionalMenuVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = { "个性化菜单管理" })
@RequestMapping("/wechat/{channelId}/management/menu/conditional")
public class ConditonalMenuManagementController extends AbstractController {
    private @Autowired ConditonalMenuService conditionalMenuServiceImpl;

    @NeedWrap
    @GetMapping
    @ApiOperation("获取个性化菜单")
    public List<ConditionalMenuVo> getConditionalMenus(@PathVariable Long channelId) throws Exception {
        return conditionalMenuServiceImpl.getActiveConditionalMenu(channelId);
    }

    @NeedWrap
    @GetMapping("/{id}")
    @ApiOperation("获取个性化菜单详情")
    public ConditionalMenuVo getConditionalMenu(@PathVariable Long channelId, @PathVariable Long id)
            throws Exception {
        return conditionalMenuServiceImpl.getActiveConditionalMenu(channelId, id);
    }

    @NeedWrap
    @PostMapping
    @ApiOperation("发布个性化菜单")
    public String publishConditionalMenu(@PathVariable Long channelId, @RequestBody ConditionalMenu conditionalMenu)
            throws Exception {
        conditionalMenu.setId(null);
        conditionalMenuServiceImpl.publishConditionalMenu(channelId, conditionalMenu);
        return "ok";
    }

    @NeedWrap
    @DeleteMapping("/{id}")
    @ApiOperation("删除个性化菜单")
    public String delConditionalMenu(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        conditionalMenuServiceImpl.delConditionalMenu(channelId, id);
        return "ok";
    }
}
