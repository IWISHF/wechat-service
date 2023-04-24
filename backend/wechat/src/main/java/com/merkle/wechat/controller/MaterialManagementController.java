package com.merkle.wechat.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import com.merkle.wechat.common.entity.MaterialAsset;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.mpnews.Mpnews;
import com.merkle.wechat.service.MaterialService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.mpnews.MpnewsCommentService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.asset.MaterialAssetVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "微信素材")
@RequestMapping("/wechat/{channelId}/management/material")
public class MaterialManagementController extends AbstractController {

    private @Autowired WechatPublicNoService pbNoServiceImpl;

    private @Autowired MaterialService materialServiceImpl;

    private @Autowired MpnewsCommentService mpnewsCommentServiceImpl;

    @NeedWrap
    @ApiOperation(value = "同步素材")
    @GetMapping(path = "/sync/{type}")
    public String syncMaterial(@PathVariable Long channelId,
            @ApiParam(allowableValues = "image,news,video,voice") @PathVariable String type) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        materialServiceImpl.syncMaterial(pbNo, type);
        return "ok";
    }

    @NeedWrap
    @ApiOperation(value = "获取图文")
    @GetMapping(path = "/mpnews/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<Mpnews> searchMpnews(@PathVariable Long channelId,
            @ApiParam(required = false) @RequestParam(defaultValue = "") String key,
            @ApiParam(required = false, allowableValues = "true,false", defaultValue = "true", name = "") @RequestParam(defaultValue = "true") boolean containsMultiNews,
            @ApiIgnore Pageable pageable) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return materialServiceImpl.searchMpnews(pbNo, key, containsMultiNews, pageable);
    }

    @NeedWrap
    @ApiOperation(value = "同步图文评论")
    @GetMapping(path = "/mpnews/comment/sync")
    public String syncMpnewsComments(@PathVariable Long channelId,
            @ApiParam(required = true) @RequestParam @Valid @NotNull Long mpnewsId,
            @ApiParam(required = true) @RequestParam @Valid @NotNull Long msgDataId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        mpnewsCommentServiceImpl.syncMpnewsComments(mpnewsId, pbNo, msgDataId);
        return "ok";
    }

    @NeedWrap
    @GetMapping(path = "/{type}")
    @ApiOperation(value = "获取图片/视频/音频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<MaterialAsset> getImage(@PathVariable Long channelId,
            @ApiParam(allowableValues = "image,video,voice") @PathVariable String type, @ApiIgnore Pageable pageable)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return materialServiceImpl.getMaterial(pbNo, type, pageable);
    }

    @NeedWrap
    @PostMapping
    @ApiOperation(value = "获取素材")
    public MaterialAssetVo getAssets(@PathVariable Long channelId, @RequestBody List<String> mediaIds)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return materialServiceImpl.getMaterail(pbNo, mediaIds);
    }
}
