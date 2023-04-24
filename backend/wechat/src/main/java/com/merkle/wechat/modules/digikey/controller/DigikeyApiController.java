package com.merkle.wechat.modules.digikey.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyAPIServiceImpl;
import com.merkle.wechat.modules.digikey.vo.CMSSearchVo;
import com.merkle.wechat.modules.digikey.vo.DigikeyKeywordSearchVo;
import com.merkle.wechat.modules.digikey.vo.DigikeyProductDetailVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/wechat/digikey")
@Api(tags = "DIGIKEY WEBSITE APIS")
public class DigikeyApiController extends AbstractController {
    private @Autowired DigikeyAPIServiceImpl apiServiceImpl;

    @NeedWrap
    @PostMapping("/api/partsearch")
    @ApiOperation(value = "search product")
    public String partSearch(@RequestBody @Valid DigikeyKeywordSearchVo vo) throws Exception {
        return apiServiceImpl.keywordSearch(vo).toString();
    }

    @NeedWrap
    @PostMapping("/api/partdetail")
    @ApiOperation(value = "product detail")
    public String partDetail(@RequestBody @Valid DigikeyProductDetailVo vo) throws Exception {
        return apiServiceImpl.productDetail(vo).toString();
    }

    @NeedWrap
    @PostMapping("/api/cms/article/search")
    @ApiOperation(value = "search cms article")
    public String searchCmsArticle(@RequestBody @Valid CMSSearchVo vo) throws Exception {
        return apiServiceImpl.cmsArticleSearch(vo).toString();
    }
}
