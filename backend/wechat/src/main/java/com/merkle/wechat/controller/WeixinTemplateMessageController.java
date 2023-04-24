package com.merkle.wechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.template.WeixinTemplate;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.modules.digikey.service.TemplateMessageServiceImpl;
import com.merkle.wechat.service.template.WeixinTemplateMessageService;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "模版管理")
@Controller
@RequestMapping("/wechat/{channelId}/management/template")
public class WeixinTemplateMessageController extends AbstractController {
    private @Autowired WeixinTemplateMessageService templateMessageServiceImpl;
    private @Autowired TemplateMessageServiceImpl tMessageServiceImpl;

    @NeedWrap
    @GetMapping("/sync")
    @ApiOperation("同步添加至账号下的模版")
    public String syncAddedTemplate(@PathVariable Long channelId) throws Exception {
        templateMessageServiceImpl.syncAddedTemplate(channelId);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation("搜索账号下的模版")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<WeixinTemplate> search(@PathVariable Long channelId, @ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "") String key) throws Exception {
        return templateMessageServiceImpl.search(channelId, key, pageable);
    }

    @NeedWrap
    @GetMapping("/active/all")
    @ApiOperation("获取所有Active模版")
    public List<WeixinTemplate> getAllActive(@PathVariable Long channelId, @RequestParam(defaultValue = "") String key) {
        return templateMessageServiceImpl.getAllActive(channelId, key);
    }

    @NeedWrap
    @GetMapping("/send")
    @ApiOperation("根据taskID 发送模板消息")
    public String commonSendTemplate(
            @PathVariable Long channelId,
            @RequestParam(required = true) @ApiParam("必须是common") String start,
            @RequestParam(required = true)Long taskId) throws Exception {
        if (start.equals("common")) {
            AsyncUtil.asyncRun(() -> {
                tMessageServiceImpl.sendTemplateMessage(taskId, channelId);
            });
        }
        return "ok";
    }
}
