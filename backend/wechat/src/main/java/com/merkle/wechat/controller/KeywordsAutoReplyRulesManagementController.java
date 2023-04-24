package com.merkle.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.KeywordsAutoReply;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.AutoReplyService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.autoreply.KeywordsAutoReplyRuleVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "关键词自动回复")
@RequestMapping("/wechat/{channelId}/management/keywords/autoreply")
public class KeywordsAutoReplyRulesManagementController extends AbstractController {
    private @Autowired AutoReplyService keywordsAutoReplyServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;

    @NeedWrap
    @PostMapping
    @ApiOperation(value = "新增关键词回复")
    public String createKeywordsAutoReply(@RequestBody KeywordsAutoReply autoReply, @PathVariable Long channelId)
            throws Exception {
        if (autoReply.getAutoReplyrules() == null || autoReply.getAutoReplyrules().size() == 0) {
            throw new ServiceWarn(ExceptionConstants.AUTOREPLY_CANT_BE_EMPTY);
        }
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        keywordsAutoReplyServiceImpl.createKeywordsAutoReply(autoReply, pbNo);
        return "ok";
    }

    @NeedWrap
    @ApiOperation(value = "开起或关闭关键词回复")
    @GetMapping("/{keywordsId}/trigger/{enable}")
    public String triggerKeywordsAutoReply(@PathVariable Long keywordsId, @PathVariable Long channelId,
            @ApiParam(allowableValues = "true,false") @PathVariable boolean enable) throws Exception {
        keywordsAutoReplyServiceImpl.triggerKeywordsAutoReply(keywordsId, channelId, enable);
        return "ok";
    }

    @NeedWrap
    @ApiOperation(value = "关键词回复详情")
    @GetMapping("/{keywordsId}")
    public KeywordsAutoReplyRuleVo getKeywordsAutoReplyDetail(@PathVariable Long keywordsId,
            @PathVariable Long channelId) throws Exception {
        return keywordsAutoReplyServiceImpl.getKeywordsAutoReplyDetail(keywordsId, channelId);
    }

    @NeedWrap
    @PutMapping("/{keywordsId}")
    @ApiOperation(value = "修改关键词回复")
    public String updateKeywordsAutoReply(@RequestBody KeywordsAutoReply autoReply, @PathVariable Long keywordsId,
            @PathVariable Long channelId) throws Exception {
        if (autoReply.getAutoReplyrules() == null || autoReply.getAutoReplyrules().size() == 0) {
            throw new ServiceWarn(ExceptionConstants.AUTOREPLY_CANT_BE_EMPTY);
        }
        keywordsAutoReplyServiceImpl.updateKeywordsAutoReply(autoReply, keywordsId, channelId);
        return "ok";
    }

    @NeedWrap
    @DeleteMapping("/{keywordsId}")
    @ApiOperation(value = "删除关键词回复")
    public String deleteKeywordsAutoReply(@PathVariable Long keywordsId, @PathVariable Long channelId)
            throws Exception {
        keywordsAutoReplyServiceImpl.delete(keywordsId, channelId);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation(value = "根据name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<KeywordsAutoReply> SearchKeywordsAutoReply(@PathVariable Long channelId,
            @RequestParam(defaultValue = "") String key, @ApiIgnore Pageable pageable) throws Exception {
        return keywordsAutoReplyServiceImpl.search(channelId, key, pageable);
    }

}
