package com.merkle.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.Article;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.service.ArticleService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "高级图文管理")
@Controller
@RequestMapping("/wechat/{channelId}/management/article")
public class ArticleManagementController extends AbstractController {
    private @Autowired ArticleService articleServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;

    @NeedWrap
    @ApiOperation("新增高级图文")
    @RequestMapping(method = RequestMethod.POST)
    public String createArticle(@RequestBody Article article, @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        articleServiceImpl.createArticle(article, pbNo);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("修改高级图文")
    @PutMapping("/{articleId}")
    public String updateArticle(@RequestBody Article article, @PathVariable Long channelId,
            @PathVariable Long articleId) throws Exception {
        articleServiceImpl.udpateArticle(article, channelId, articleId);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("删除高级图文")
    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, @PathVariable Long channelId) throws Exception {
        articleServiceImpl.delete(articleId, channelId);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation("根据Title搜索高级图文")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<Article> SearchArticle(@PathVariable Long channelId, String key, @ApiIgnore Pageable pageable)
            throws Exception {
        return articleServiceImpl.search(channelId, key, pageable);
    }

    @NeedWrap
    @ApiOperation("获取高级图文详情")
    @GetMapping("/{articleId}")
    public Article getArticle(@PathVariable Long articleId, @PathVariable Long channelId) throws Exception {
        return articleServiceImpl.findArticle(articleId, channelId);
    }

}
