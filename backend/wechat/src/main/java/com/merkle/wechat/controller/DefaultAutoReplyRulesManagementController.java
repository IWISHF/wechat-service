package com.merkle.wechat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.DefaultAutoReply;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.DefaultAutoReplyService;
import com.merkle.wechat.vo.autoreply.DefaultAutoReplyVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "默认规则自动回复")
@RequestMapping("/wechat/{channelId}/management/default/autoreply")
public class DefaultAutoReplyRulesManagementController extends AbstractController {
    private @Autowired DefaultAutoReplyService defaultAutoReplyServiceImpl;

    @NeedWrap
    @ApiOperation(value = "开起或关闭默认规则")
    @GetMapping("/{defaultRuleId}/trigger/{enable}")
    public String triggerKeywordsAutoReply(@PathVariable Long defaultRuleId, @PathVariable Long channelId,
            @ApiParam(allowableValues = "true,false") @PathVariable boolean enable) throws Exception {
        defaultAutoReplyServiceImpl.triggerDefaultAutoReply(defaultRuleId, channelId, enable);
        return "ok";
    }

    @NeedWrap
    @PutMapping("/{defaultRuleId}")
    @ApiOperation(value = "修改默认规则")
    public String updateKeywordsAutoReply(@RequestBody DefaultAutoReply autoReply, @PathVariable Long defaultRuleId,
            @PathVariable Long channelId) throws Exception {
        if (autoReply.getAutoReplyrules() == null || autoReply.getAutoReplyrules().size() == 0) {
            throw new ServiceWarn(ExceptionConstants.AUTOREPLY_CANT_BE_EMPTY);
        }
        defaultAutoReplyServiceImpl.updateDefaultAutoReply(autoReply, defaultRuleId, channelId);
        return "ok";
    }

    @NeedWrap
    @GetMapping
    @ApiOperation(value = "获取默认规则")
    public List<DefaultAutoReply> getDefaultAutoReplys(@PathVariable Long channelId) {
        return defaultAutoReplyServiceImpl.getDefaultAutoReplys(channelId);
    }
    
//    @NeedWrap
//    @GetMapping("/generate")
//    @ApiOperation(value = "生成默认规则")
//    public void createDefault(@PathVariable Long channelId) throws Exception {
//        defaultAutoReplyServiceImpl.initDefaultRule(wechatPbNoServiceImpl.findByIdOrThrowNotExistException(channelId));
//    }


    @NeedWrap
    @ApiOperation(value = "默认回复详情")
    @GetMapping("/{defaultAutoReplyId}")
    public DefaultAutoReplyVo getKeywordsAutoReplyDetail(@PathVariable Long defaultAutoReplyId,
            @PathVariable Long channelId) throws Exception {
        return defaultAutoReplyServiceImpl.getDefaultAutoReplyDetail(defaultAutoReplyId, channelId);
    }
}
