package com.merkle.wechat.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.ImageAssets;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.ImageAssetsService;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "图片管理")
@RequestMapping("/wechat/{channelId}/management/asset/image")
public class ImageAssetsManagementController extends AbstractController {

    private @Autowired ImageAssetsService imageAssetsServiceImpl;

    @NeedWrap
    @ApiOperation(value = "单张上传图片")
    @PostMapping(path = "/upload")
    public String upload(@ApiParam(required = true, value = "limit size 1M") @RequestParam("pic") MultipartFile pic,
            @PathVariable Long channelId) throws Exception {
        return imageAssetsServiceImpl.createPicture(pic, channelId);
    }

    @NeedWrap
    @ApiOperation(value = "获取图片")
    @GetMapping("/get/{fileId}")
    public void getImage(@PathVariable Long channelId, @PathVariable Long fileId, HttpServletResponse response)
            throws Exception {
        ImageAssets image = Optional.ofNullable(imageAssetsServiceImpl.findImageAsset(channelId, fileId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        ServletOutputStream out = response.getOutputStream();
        response.setHeader("content-type", image.getType());
        // response.addHeader("content-disposition",
        // "attachment;filename=" + new String(image.getFileName().getBytes(),
        // "ISO8859-1"));

        out.write(image.getPic());
        out.flush();
        out.close();
    }

    @NeedWrap
    @ApiOperation(value = "单张删除图片")
    @DeleteMapping(path = "/{fileId}")
    public String upload(@PathVariable Long channelId, @PathVariable Long fileId) throws Exception {
        imageAssetsServiceImpl.deleteImage(fileId, channelId);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation(value = "搜索图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<ImageAssets> searchImage(@PathVariable Long channelId, @ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "") String key) throws Exception {
        return imageAssetsServiceImpl.search(channelId, pageable, key);
    }

    @NeedWrap
    @ApiOperation(value = "获取所有图片")
    @GetMapping("/all")
    public List<ImageAssets> allImage(@PathVariable Long channelId) throws Exception {
        return imageAssetsServiceImpl.getAll(channelId);
    }
}
