package com.merkle.wechat.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.jobs.BaseJob;
import com.merkle.wechat.common.entity.jobs.JobCommand;
import com.merkle.wechat.service.jobs.JobManagementService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.jobs.JobInvalidVo;
import com.merkle.wechat.vo.jobs.TemplateMessageJobVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "Job管理")
@RequestMapping("/wechat/{channelId}/job/management")
public class JobManagementController extends AbstractController {
    @Autowired
    private JobManagementService jobManager;

    @NeedWrap
    @PostMapping("/create")
    @ApiOperation(value = "Create a new job")
    public Map<String, Object> create(@RequestBody TemplateMessageJobVo job, HttpServletRequest request) throws Exception {

        return jobManager.createJob(job.getBaseJob(), job.getCommands(), request.getHeader("x-auth-token"));
    }

    @NeedWrap
    @PostMapping("/{jobId}/execute")
    @ApiOperation(value = "Execute a speical job through jobId")
    public Map<String, Object> executeJob(@PathVariable Long jobId) throws Exception {

        return jobManager.executeJob(jobId);
    }

    @NeedWrap
    @PostMapping("/preview")
    @ApiOperation(value = "在发送前进行效果预览")
    public Map<String, Object> previewJob(@RequestBody TemplateMessageJobVo job) throws Exception {

        return jobManager.previewJob(job.getBaseJob(), job.getCommands());
    }

    @NeedWrap
    @PostMapping("/set/invalid")
    @ApiOperation(value = "将job状态改为invalid")
    public String invalidJob(@RequestBody JobInvalidVo job) throws Exception {

        return jobManager.invalidJob(job);
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation("搜索Job列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<BaseJob> search(@PathVariable Long channelId,
            Pageable pageable) throws Exception {

        return jobManager.searchJobs(channelId, 1L, pageable);
    }

    @NeedWrap
    @GetMapping("/search/commands")
    @ApiOperation("搜索Job commands 列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<JobCommand> searchCommands(@RequestParam Long jobId, Pageable pageable) throws Exception {

        return jobManager.searchJobCommands(jobId, pageable);
    }
}
