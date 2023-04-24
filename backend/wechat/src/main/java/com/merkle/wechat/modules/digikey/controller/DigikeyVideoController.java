package com.merkle.wechat.modules.digikey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.digikey.video.DigikeyVideo;
import com.merkle.wechat.common.entity.digikey.video.DigikeyVideoGroup;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyVideoGroupServiceImpl;
import com.merkle.wechat.modules.digikey.service.DigikeyVideoServiceImpl;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "DIGIKEY VIDEO")
@RequestMapping("/wechat/digikey/video")
public class DigikeyVideoController extends AbstractController {
    private @Autowired DigikeyVideoServiceImpl digikeyVideoServiceImpl;
    private @Autowired DigikeyVideoGroupServiceImpl digikeyVideoGroupServiceImpl;

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation(value = "搜索视频")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<DigikeyVideo> searchImage(@ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "") @ApiParam(value = "groupIds 1,2,3") String groups) throws Exception {
        return digikeyVideoServiceImpl.search(pageable, key, groups);
    }

    @NeedWrap
    @GetMapping("/group/search")
    @ApiOperation(value = "搜索视频分组")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<DigikeyVideoGroup> searchVideoGroup(@ApiIgnore Pageable pageable) throws Exception {
        return digikeyVideoGroupServiceImpl.search(pageable);
    }

    @NeedWrap
    @GetMapping("/{id}")
    @ApiOperation(value = "获取视频")
    public DigikeyVideo findById(@PathVariable Long id) {
        return digikeyVideoServiceImpl.findById(id);
    }
}
