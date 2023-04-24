package com.merkle.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.merkle.wechat.annotation.NeedWrap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Danny Wang
 *
 */
@Controller
@Api(tags = "图片管理")
@RequestMapping("/wechat/{channelId}/management/asset")
public class AssetsController extends AbstractController {

    @NeedWrap
    @ApiOperation(value = "上传文件")
    @PostMapping(path = "/upload")
    public String upload(@ApiParam(required = true, value = "limit size 1M") @RequestParam("file") MultipartFile file,
            @PathVariable Long channelId, @RequestParam("type") String type) {
        return "";
    }

}
