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
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.service.TagService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.tag.TagVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "标签管理")
@RequestMapping("/wechat/{channelId}/management/tag")
public class TagManangementController extends AbstractController {

    private @Autowired TagService tagServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;

    @NeedWrap
    @GetMapping("/sync")
    @ApiOperation("同步微信标签")
    public String syncTagFromWechat(@PathVariable Long channelId) throws Exception {
        tagServiceImpl.syncTagFromWechat(pbNoServiceImpl.findByIdOrThrowNotExistException(channelId));
        return "ok";
    }

    @NeedWrap
    @PostMapping
    @ApiOperation("创建标签")
    public String createTag(@PathVariable Long channelId, @RequestBody Tag tag) throws Exception {
        tagServiceImpl.createTag(pbNoServiceImpl.findByIdOrThrowNotExistException(channelId), tag);
        return "ok";
    }

    @NeedWrap
    @PutMapping("/{tagId}")
    @ApiOperation("修改标签")
    public String updateTag(@PathVariable Long channelId, @PathVariable Long tagId, @RequestBody Tag tag)
            throws Exception {
        tagServiceImpl.updateTag(pbNoServiceImpl.findByIdOrThrowNotExistException(channelId), tagId, tag);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/weixin/search")
    @ApiOperation("搜索微信标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<TagVo> searchWeixinTag(@RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "-1") Long groupId, @PathVariable Long channelId, Pageable pageable)
            throws Exception {
        return tagServiceImpl.searchWeixinTag(key, groupId, pbNoServiceImpl.findByIdOrThrowNotExistException(channelId),
                pageable);
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation("搜索所有标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<TagVo> searchTag(@RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "-1") Long groupId, @PathVariable Long channelId, Pageable pageable)
            throws Exception {
        return tagServiceImpl.searchTag(key, groupId, pbNoServiceImpl.findByIdOrThrowNotExistException(channelId),
                pageable);
    }

    // TODO：删除标签及拥有此标签的用户关系
    @NeedWrap
    @ApiOperation("删除标签")
    @DeleteMapping("/{id}")
    public String deleteTag(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        return tagServiceImpl.deleteTag(pbNoServiceImpl.findByIdOrThrowNotExistException(channelId), id);
    }

}
