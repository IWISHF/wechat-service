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
import com.merkle.wechat.common.entity.loyalty.LoyaltyRewardConfig;
import com.merkle.wechat.service.loyalty.DynamicValueService;
import com.merkle.wechat.service.loyalty.LoyaltyRewardConfigService;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "LoyaltyReward管理")
@Controller
@RequestMapping("/wechat/{channelId}/management/loyalty/rewards")
public class LoyaltyRewardConfigController extends AbstractController {
    private @Autowired LoyaltyRewardConfigService loyaltyRewardConfigServiceImpl;
    private @Autowired DynamicValueService dynamicValueServiceImpl;

    @NeedWrap
    @PostMapping("/config")
    @ApiOperation("创建RewardConfig")
    public String create(@PathVariable Long channelId, @RequestBody @Valid LoyaltyRewardConfig config)
            throws Exception {
        loyaltyRewardConfigServiceImpl.create(channelId, config);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/config/{id}")
    @ApiOperation("获取RewardConfig")
    public LoyaltyRewardConfig getDetial(@PathVariable Long channelId, @PathVariable @NotNull @Valid Long id)
            throws Exception {
        return loyaltyRewardConfigServiceImpl.findOne(channelId, id);
    }

    @NeedWrap
    @PostMapping("/config/update")
    @ApiOperation("更新RewardConfig")
    public String update(@PathVariable Long channelId, @RequestBody @Valid LoyaltyRewardConfig config)
            throws Exception {
        loyaltyRewardConfigServiceImpl.update(channelId, config);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/config/del/{id}")
    @ApiOperation("删除RewardConfig")
    public String delete(@PathVariable Long channelId, @PathVariable @NotNull @Valid Long id) throws Exception {
        loyaltyRewardConfigServiceImpl.delete(channelId, id);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation("搜索RewardConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<LoyaltyRewardConfig> search(@PathVariable Long channelId, @ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "") String key) throws Exception {
        return loyaltyRewardConfigServiceImpl.search(channelId, key, pageable);
    }

    @NeedWrap
    @GetMapping("/config/dynamicvalue")
    @ApiOperation("获取奖励模版动态属性值")
    public List<String> dynamicValue(@PathVariable Long channelId) throws Exception {
        return dynamicValueServiceImpl.dynamicValue();
    }

}
