package com.merkle.wechat.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.follower.FollowerShipAddress;
import com.merkle.wechat.common.entity.follower.MemberAttribute;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.service.RewardsRedeemLogService;
import com.merkle.wechat.service.follower.FollowerBindInfoService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.follower.FollowerShipAddressService;
import com.merkle.wechat.service.follower.MemberAttributeService;
import com.merkle.wechat.vo.follower.FollowerApiInfoVo;
import com.merkle.wechat.vo.follower.FollowerCheckVo;
import com.merkle.wechat.vo.follower.FollowerRelatedVo;
import com.merkle.wechat.vo.follower.TicketCheckVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "粉丝信息API")
@RequestMapping(path = "/wechat/api/follower")
public class FollowerAPIController extends AbstractController {
    private @Autowired FollowerService followerServiceImpl;
    private @Autowired FollowerBindInfoService followerBindInfoServiceImpl;
    private @Autowired FollowerShipAddressService followerShipAddressServiceImpl;
    private @Autowired RewardsRedeemLogService rewardRedeemLogServiceImpl;
    private @Autowired MemberAttributeService memberAttributeServiceImpl;

    @NeedWrap
    @GetMapping("/{openid}")
    @ApiOperation("获取粉丝详情包含绑定信息")
    public FollowerApiInfoVo getFollowerByOpenId(@PathVariable String openid, @RequestParam(name = "includes", required = false) String includes) throws Exception {
        if (null != includes) {
            return followerBindInfoServiceImpl.findFollowerWithIncludes(openid, includes);
        }
        return followerBindInfoServiceImpl.getFollowerInfo(openid);
    }

    @NeedWrap
    @GetMapping("/ship/address/{openid}")
    @ApiOperation("获取会员收件地址列表")
    public List<FollowerShipAddress> shipAddress(@PathVariable @Valid @NotEmpty String openid) throws Exception {
        return followerShipAddressServiceImpl.getByOpenid(openid);
    }

    @NeedWrap
    @PostMapping("/ship/address")
    @ApiOperation("绑定会员收件地址")
    public String shipAddress(@RequestBody @Valid FollowerShipAddress address) throws Exception {
        followerShipAddressServiceImpl.create(address);
        return "ok";
    }

    @NeedWrap
    @PostMapping("/ship/address/{id}")
    @ApiOperation("修改会员收件地址")
    public String shipAddressUpdate(@PathVariable Long id, @RequestBody @Valid FollowerShipAddress address)
            throws Exception {
        followerShipAddressServiceImpl.update(id, address);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/ship/address/{id}/del")
    @ApiOperation("删除会员收件地址")
    public String delShipAddressUpdate(@PathVariable Long id) throws Exception {
        followerShipAddressServiceImpl.delete(id);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/ship/address/{id}/detail")
    @ApiOperation("获取会员收件地址")
    public FollowerShipAddress getShipAddressUpdate(@PathVariable Long id) throws Exception {
        return followerShipAddressServiceImpl.getById(id);
    }

    @NeedWrap
    @PostMapping("/bind/info")
    @ApiOperation("绑定会员信息")
    public String bindInfo(@RequestBody @Valid FollowerBindInfo bindInfo, @RequestParam(name = "attribute_type", required = false) String type) throws Exception {
        followerBindInfoServiceImpl.create(bindInfo);
        if (null != type && MemberAttribute.AttributeType.ORDER_CODE.toString().toLowerCase().equals(type)) {
            if (FollowerBindInfo.isEnginnerOrStudent(bindInfo)) {
                MemberAttribute attribute = memberAttributeServiceImpl.genereateMemberAttribute(bindInfo, type, 1);
                return attribute.getValue();
            }
        }
        return "ok";
    }

    @NeedWrap
    @PostMapping("/bind/info/{id}")
    @ApiOperation("修改绑定会员信息")
    public String bindInfoUpdate(@PathVariable Long id, @RequestBody @Valid FollowerBindInfo bindInfo, @RequestParam(name = "attribute_type", required = false) String type)
            throws Exception {
        followerBindInfoServiceImpl.update(id, bindInfo);
        if (null != type && MemberAttribute.AttributeType.ORDER_CODE.toString().toLowerCase().equals(type)) {
            if (FollowerBindInfo.isEnginnerOrStudent(bindInfo)) {
                MemberAttribute attribute = memberAttributeServiceImpl.genereateMemberAttribute(bindInfo, type, 1);
                return attribute.getValue();
            }
        }
        return "ok";
    }

    @NeedWrap
    @GetMapping("/bind/check/{openid}")
    public boolean checkBind(@PathVariable String openid) throws Exception {
        Optional<FollowerBindInfo> follower = Optional.ofNullable(followerBindInfoServiceImpl.findOneByOpenid(openid));
        if (follower.isPresent()) {
            return true;
        }
        return false;
    }

    @NeedWrap
    @GetMapping("/check/{openid}")
    public boolean check(@PathVariable String openid) throws Exception {
        Optional<Follower> follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid));
        if (follower.isPresent() && follower.get().getSubscribe().intValue() != 0) {
            return true;
        }
        return false;
    }

    @NeedWrap
    @PostMapping("/v2/check/{openid}")
    public FollowerRelatedVo checkFollowerRelatedInfo(@PathVariable String openid, @RequestBody FollowerCheckVo filters)
            throws Exception {

        return followerServiceImpl.checkFollowerRelated(filters, openid);
    }

    @NeedWrap
    @ApiOperation("入场券检录")
    @PostMapping("/loyalty/ticket/check")
    public String checkTicket(@RequestBody @Valid TicketCheckVo vo) throws Exception {
        rewardRedeemLogServiceImpl.checkin(vo);
        return "ok";
    }

    @NeedWrap
    @ApiOperation("所有奖励")
    @PostMapping("/{openid}/loyalty/rewards")
    public List<RewardsRedeemLog> getActiveRewards(@PathVariable String openid, @RequestParam Long channelId)
            throws Exception {
        return rewardRedeemLogServiceImpl.getAllRewards(openid, channelId);
    }

}
