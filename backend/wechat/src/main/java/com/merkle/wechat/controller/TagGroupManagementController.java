package com.merkle.wechat.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.service.TagGroupService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "标签组管理")
@RequestMapping("/wechat/{channelId}/management/taggroup")
public class TagGroupManagementController extends AbstractController {
    private @Autowired TagGroupService tagGroupServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;

    @NeedWrap
    @GetMapping("/all")
    @ApiOperation("获取所有分组")
    public List<TagGroup> getAllTagGroup(@PathVariable Long channelId) throws Exception {
        return tagGroupServiceImpl.getAllGroup(channelId);
    }

    @NeedWrap
    @GetMapping("/all/pageable")
    @ApiOperation("分页获取所有分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<TagGroup> getAllTagGroup(@PathVariable Long channelId, Pageable pageable) throws Exception {
        return tagGroupServiceImpl.getAllGroup(channelId, pageable);
    }

    @NeedWrap
    @PostMapping
    @ApiOperation("新增标签组")
    public String createTagGroup(@RequestBody @Valid TagGroup group, @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        tagGroupServiceImpl.createTagGroup(group, pbNo);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("删除标签组")
    @DeleteMapping("/{groupId}")
    public String deleteTagGroup(@PathVariable Long groupId, @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        tagGroupServiceImpl.deleteTagGroup(groupId, pbNo);
        return "ok";
    }

    @NeedWrap
    @PutMapping("/{groupId}")
    @ApiOperation("修改标签组")
    public String updateTagGroup(@RequestBody @Valid TagGroup group, @PathVariable Long groupId,
            @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        tagGroupServiceImpl.updateTagGroup(group, groupId, pbNo);
        return "ok";
    }
}
