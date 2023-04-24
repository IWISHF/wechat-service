package com.merkle.wechat.modules.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.demo.service.DemoTagService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import weixin.popular.bean.user.TagsGetidlistResult;

@Controller
@RequestMapping("/wechat/demo")
@Api(tags = "DemoTag")
public class DemoTagController extends AbstractController{
    
    private @Autowired DemoTagService tagServiceImpl;
    
    @NeedWrap
    @RequestMapping("/oauth/tag/create")
    @ApiOperation("tag create")
    public Integer findOrCreateTag(String tagStr, String appid) throws Exception {
        return tagServiceImpl.findOrCreateTag(tagStr, appid);
    }
    
    @NeedWrap
    @RequestMapping("/oauth/tag")
    @ApiOperation("tag follower")
    public void tagFollower(String openid, Integer tagid, String channel) throws Exception {
        if (channel.equals("nanjing")) {
            tagServiceImpl.tagFollower(openid, tagid, "wx50f190ca3a0ee011");
        } else {
            tagServiceImpl.tagFollower(openid, tagid, "wx9f5a246a8ff9c3f3");
        }
    }
    
    @NeedWrap
    @RequestMapping("/oauth/group")
    @ApiOperation("tag group")
    public void groupFollower(String openid, Integer tagid, String channel) throws Exception {
        if (channel.equals("nanjing")) {
            tagServiceImpl.groupFollower(openid, tagid, "wx50f190ca3a0ee011");
        } else {
            tagServiceImpl.groupFollower(openid, tagid, "wx9f5a246a8ff9c3f3");
        }
    }
    
    @NeedWrap
    @RequestMapping("/oauth/taglist")
    @ApiOperation("tag list")
    public TagsGetidlistResult getTagList(String openid, String channel) throws Exception {
        if (channel.equals("nanjing")) {
            return tagServiceImpl.getTagList("wx50f190ca3a0ee011", openid);
        } else {
            return tagServiceImpl.getTagList("wx9f5a246a8ff9c3f3", openid);
        }
    }
}
