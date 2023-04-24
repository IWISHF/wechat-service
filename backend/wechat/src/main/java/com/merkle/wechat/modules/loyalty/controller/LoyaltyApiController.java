package com.merkle.wechat.modules.loyalty.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.modules.loyalty.vo.DigikeyCampaignVideoVo;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyEventRecordVo;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyEventsVo;
import com.merkle.wechat.modules.loyalty.vo.LoyaltyRewardsRedeemVo;
import com.merkle.wechat.service.WechatPublicNoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Loyalty API")
@RestController
@RequestMapping(path = "/wechat/api/loyalty")
public class LoyaltyApiController extends AbstractController {
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired WechatPublicNoService channelServiceImpl;

    @ApiOperation("获取channel对应的所有Rewards")
    @GetMapping(path = "/rewards")
    public String rewards(@ApiParam(value = "channelId") @RequestParam Long channelId,
            @ApiParam(value = "reward组过滤条件如A,B 代表查询A和B组的数据") @RequestParam(defaultValue = "") String filter)
                    throws Exception {
        if (channelId == null) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        return loyaltyServiceImpl.rewards(channelId, filter).toString();
    }

    @PostMapping(path = "/approve/comment")
    @ApiOperation("临时根据openid发送评论成功加分事件, Start参数的值为pingpong否则调不通️")
    public String approveComment(@ApiParam(value = "channelId") @RequestParam Long channelId,
            @RequestParam String start, @RequestParam String openid) throws Exception {
        if (channelId == null || !start.equals("pingpong")) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        return loyaltyServiceImpl.recordCommentBonusEvent(channelId, openid);
    }

    @ApiOperation("获取channel对应的所有Reward Group，包含对应的rewards")
    @GetMapping(path = "/reward/groups")
    public String rewardGroups(@ApiParam(value = "channelId") @RequestParam Long channelId) throws Exception {
        if (channelId == null) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        return loyaltyServiceImpl.rewardGroups(channelId).toString();
    }

    @ApiOperation("获取用户的Specific Rewards")
    @GetMapping(path = "/customer/rewards")
    public String customerRewards(@ApiParam(value = "openid") @Valid @NotEmpty @RequestParam String id,
            @RequestParam(defaultValue = "") String filter) throws Exception {
        return loyaltyServiceImpl.customerRewards(id, filter).toString();
    }

    @ApiOperation("获取用户的基础信息")
    @GetMapping(path = "/customer/show/basic")
    public ResponseData customerShowBasic(@ApiParam(value = "openid") @Valid @NotEmpty @RequestParam String id)
            throws Exception {
        return loyaltyServiceImpl.customerShowBasic(id);
    }

    @ApiOperation("获取用户的信息包含rewards 和 rewards_stats")
    @GetMapping(path = "/customer/show/rewards")
    public ResponseData customerShowRewards(@ApiParam(value = "openid") @Valid @NotEmpty @RequestParam String id)
            throws Exception {
        return loyaltyServiceImpl.customerShowRewards(id);
    }

    @ApiOperation("获取用户的Coupons")
    @GetMapping(path = "/customer/coupons")
    public String customerShowCoupons(@ApiParam(value = "openid") @Valid @NotEmpty @RequestParam String id)
            throws Exception {
        return loyaltyServiceImpl.customerCoupons(id).toString();
    }

    @ApiOperation("获取用户的事件")
    @PostMapping(path = "/customer/events")
    public String cutomerEvents(@RequestBody @Valid LoyaltyEventsVo vo) throws Exception {
        return loyaltyServiceImpl.customerEvents(vo).toString();
    }

    @ApiOperation("兑换Rewards")
    @PostMapping(path = "/reward/redeem")
    public String rewardRedeem(@RequestBody @Valid LoyaltyRewardsRedeemVo vo) throws Exception {
        if (vo.getRewardType().equals("0") && (StringUtils.isEmpty(vo.getPhone()) || StringUtils.isEmpty(vo.getName())
                || StringUtils.isEmpty(vo.getAddress()))) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }

        return loyaltyServiceImpl.redeemReward(vo).toString();
    }

    @ApiOperation("record seeed video 1")
    @PostMapping(path = "/record/seeed/am")
    public String recordSeeed1(@RequestBody @Valid DigikeyCampaignVideoVo vo) throws Exception {
        return loyaltyServiceImpl.recordSeeed(vo.getId(), "seeed_video_1").toString();
    }

    @ApiOperation("record seeed video 2")
    @PostMapping(path = "/record/seeed/br")
    public String recordSeeed2(@RequestBody @Valid DigikeyCampaignVideoVo vo) throws Exception {
        return loyaltyServiceImpl.recordSeeed(vo.getId(), "seeed_video_2").toString();
    }

    @ApiOperation("record seeed video 3")
    @PostMapping(path = "/record/seeed/ca")
    public String recordSeeed3(@RequestBody @Valid DigikeyCampaignVideoVo vo) throws Exception {
        return loyaltyServiceImpl.recordSeeed(vo.getId(), "seeed_video_3").toString();
    }

    @ApiOperation("record seeed video 4")
    @PostMapping(path = "/record/seeed/de")
    public String recordSeeed4(@RequestBody @Valid DigikeyCampaignVideoVo vo) throws Exception {
        return loyaltyServiceImpl.recordSeeed(vo.getId(), "seeed_video_4").toString();
    }

    @ApiOperation("record seeed video 5")
    @PostMapping(path = "/record/seeed/eu")
    public String recordSeeed5(@RequestBody @Valid DigikeyCampaignVideoVo vo) throws Exception {
        return loyaltyServiceImpl.recordSeeed(vo.getId(), "seeed_video_5").toString();
    }

    @ApiOperation("Record a specific event")
    @PostMapping({ "/{channelId}/record/event" })
    public String recordEvent(@RequestBody @Valid final LoyaltyEventRecordVo vo, @PathVariable @Valid final Long channelId) throws Exception {
        final WechatPublicNo channel = this.channelServiceImpl.findOneById(channelId);
        return this.loyaltyServiceImpl.recordEvent(vo.getId(), channel.getAuthorizerAppid(), vo.getType(), vo.getValue()).toString();
    }

    @ApiOperation("获取所有积分规则")
    @GetMapping(path = "/point/groups")
    public String pointGroups(@ApiParam(value = "channelId") @Valid @NotEmpty @RequestParam Long channelId)
            throws Exception {
        return loyaltyServiceImpl.pointRules(channelId).toString();
    }
}
