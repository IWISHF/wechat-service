package com.merkle.wechat.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.service.MiniProgramService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.follower.FollowerSyncService;
import com.merkle.wechat.vo.BindMiniProgramVo;
import com.merkle.wechat.vo.thridparty.WechatPublicNoVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "公众号管理")
@RequestMapping("/wechat/account/{accountId}/management")
public class WechatPublicNoManagementController extends AbstractController {
    private @Autowired WechatPublicNoService wechatPublicNoServiceImpl;
    private @Autowired FollowerService followerServiceImpl;
    private @Autowired FollowerSyncService followerSyncServiceImpl;
    private @Autowired MiniProgramService miniProgramServiceImpl;

    @NeedWrap
    @ApiOperation("分页获取平台授权公众号")
    @RequestMapping("/authenticated/publicno/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public List<WechatPublicNoVo> getAllAuthenticatedPublicNo(@PathVariable Long accountId,
            @ApiIgnore Pageable pageable) {
        return wechatPublicNoServiceImpl.getAllByAccountId(accountId, pageable);
    }

    @NeedWrap
    @RequestMapping("/follower/sync")
    public void syncFollowerForAppId(String appid) throws Exception {
        followerServiceImpl.syncFollowersFromWechat(appid);
    }

    @NeedWrap
    @RequestMapping("/follower/sync/update")
    public boolean updateFollowerForAppId(String appid) throws Exception {
        AsyncUtil.asyncRun(() -> {
            try {
                followerSyncServiceImpl.syncLatestFollowerInfoFromWechat(appid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @NeedWrap
    @PostMapping("/mini/bind")
    @ApiOperation("绑定小程序账号")
    public String bindMiniProgram(@RequestBody @Valid BindMiniProgramVo vo, @PathVariable Long accountId) {
        miniProgramServiceImpl.bind(vo, accountId);
        return "ok";
    }
}
