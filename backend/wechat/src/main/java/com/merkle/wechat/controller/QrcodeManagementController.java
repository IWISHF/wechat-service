package com.merkle.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.service.QrcodeService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.qrcode.QrcodeDetailVo;
import com.merkle.wechat.vo.qrcode.QrcodeVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "二维码管理")
@RequestMapping("/wechat/{channelId}/management/qrcode")
public class QrcodeManagementController extends AbstractController {

    private @Autowired QrcodeService qrcodeServiceImpl;

    @NeedWrap
    @PostMapping("/create")
    @ApiOperation(value = "废弃", hidden = true)
    @Deprecated
    public void createQrcodeDeprecated(@PathVariable Long channelId, String tag, String type) {
        if (type.equals("LimitId")) {
            qrcodeServiceImpl.createLimitQrcode(channelId, Integer.valueOf(tag));
        } else if (type.equals("LimitStr")) {
            qrcodeServiceImpl.createLimitStrQrcode(channelId, tag);
        } else if (type.equals("TempId")) {
            qrcodeServiceImpl.createTempSceneIdQrcode(channelId, Long.valueOf(tag));
        } else if (type.equals("TempStr")) {
            qrcodeServiceImpl.createTempSceneStrQrcode(channelId, tag);
        }
    }

    @NeedWrap
    @PostMapping
    @ApiOperation(value = "创建二维码")
    public String createQrcode(@RequestBody QrcodeVo vo, @PathVariable Long channelId) throws Exception {
        qrcodeServiceImpl.createQrcode(vo, channelId);
        return "ok";
    }

    @NeedWrap
    @PutMapping("/{qrcodeId}")
    @ApiOperation(value = "修改二维码")
    public String updateQrcode(@RequestBody QrcodeVo vo, @PathVariable Long channelId, @PathVariable Long qrcodeId)
            throws Exception {
        qrcodeServiceImpl.updateQrcode(vo, channelId, qrcodeId);
        return "ok";
    }
    
    @NeedWrap
    @ApiOperation(value = "二维码详情")
    @GetMapping("/{qrcodeId}")
    public QrcodeDetailVo getKeywordsAutoReplyDetail(@PathVariable Long qrcodeId,
            @PathVariable Long channelId) throws Exception {
        return qrcodeServiceImpl.getQrcodeDetail(qrcodeId, channelId);
    }

    @NeedWrap
    @ApiOperation(value = "开起或关闭二维码")
    @GetMapping("/{qrcodeId}/trigger/{enable}")
    public String triggerQrcode(@PathVariable Long qrcodeId, @PathVariable Long channelId,
            @ApiParam(allowableValues = "true,false") @PathVariable boolean enable) throws Exception {
        qrcodeServiceImpl.triggerQrcode(qrcodeId, channelId, enable);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation(value = "根据name搜索二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<QrcodeVo> SearchQrcode(@PathVariable Long channelId, @RequestParam(defaultValue = "") String key,
            @ApiIgnore Pageable pageable) throws Exception {
        return qrcodeServiceImpl.search(channelId, key, pageable);
    }

}
