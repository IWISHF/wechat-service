package com.merkle.wechat.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.batch.BatchTaskService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.batch.BatchTaskCreateVo;
import com.merkle.wechat.vo.batch.BatchTaskVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = { "群发管理" })
@RequestMapping("/wechat/{channelId}/management/batch/task")
public class BatchTaskManagementController extends AbstractController {
    private @Autowired BatchTaskService batchTaskServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;

    @NeedWrap
    @PostMapping
    @ApiOperation("创建群发")
    public String createBatchTask(@PathVariable Long channelId, @Valid @RequestBody BatchTaskCreateVo vo)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        if (vo.getTriggerDate() != null && vo.getTriggerDate().before(new Date())) {
            throw new ServiceWarn(ExceptionConstants.BATCH_TASK_CREATE_TRIGGER_DATE_NOT_FUTURE_ERROR);
        }
        batchTaskServiceImpl.createBatchTask(pbNo, vo);
        return "ok";
    }

    @NeedWrap
    @PostMapping("/preview")
    @ApiOperation("预览群发")
    public String previewBatchTask(@PathVariable Long channelId, @Valid @RequestBody AutoReplyRule rule)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return batchTaskServiceImpl.previewBatchTask(pbNo, rule);
    }

    @NeedWrap
    @DeleteMapping("/{id}")
    @ApiOperation("删除群发")
    public String deleteBatchTask(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        batchTaskServiceImpl.deleteBatchTask(pbNo, id);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation(value = "根据name查找群发")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<BatchTaskVo> searchBatchTaskByName(@PathVariable Long channelId,
            @RequestParam(defaultValue = "") String key, @ApiIgnore Pageable pageable) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return batchTaskServiceImpl.searchByName(pbNo, key, pageable);
    }

    @NeedWrap
    @GetMapping("/{id}")
    @ApiOperation("群发详情")
    public BatchTaskVo batchTaskDetail(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return batchTaskServiceImpl.getBatchTask(pbNo, id);
    }

}
