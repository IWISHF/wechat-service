package com.merkle.wechat.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.service.RewardsRedeemLogService;
import com.merkle.wechat.service.template.WeixinTemplateMessageService;
import com.merkle.wechat.vo.Pagination;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "LoyaltyReward兑换记录管理")
@Controller
@RequestMapping("/wechat/{channelId}/management/loyalty/rewards/redeemed")
public class LoyaltyRewardRedeemedManagementController extends AbstractController {
    private @Autowired RewardsRedeemLogService rewardsRedeemLogServciceImpl;
    private @Autowired WeixinTemplateMessageService weixinTemplateMessageServiceImpl;

    /**
     * @param channelId
     * @param pageable
     * @param key
     * @param type
     *            0是实体,1是虚拟
     * @return
     * @throws Exception
     */
    @NeedWrap
    @GetMapping("/search")
    @ApiOperation("搜索兑换记录 支持手机号，收件人姓名，地址, 奖品名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<RewardsRedeemLog> search(@PathVariable Long channelId, @ApiIgnore Pageable pageable,
            @RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0", required = true) String type)
            throws Exception {
        return rewardsRedeemLogServciceImpl.search(channelId, key, type, pageable);
    }

    @NeedWrap
    @ApiOperation("发送订单物流信息")
    @GetMapping(path = "/{logid}/express/send")
    public String sendExpressTemplateMessage(@PathVariable Long logid, @PathVariable Long channelId,
            @RequestParam @Valid @NotEmpty String trackingCode) throws Exception {
        weixinTemplateMessageServiceImpl.sendExpressTemplateMessage(trackingCode, logid, channelId);
        return "ok";
    }

}
