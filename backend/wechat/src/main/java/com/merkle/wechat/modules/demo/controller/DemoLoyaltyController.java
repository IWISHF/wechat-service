package com.merkle.wechat.modules.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.demo.service.DemoLoyaltyService;
import com.merkle.wechat.modules.demo.vo.LoyaltyRequestVo;

@RestController
public class DemoLoyaltyController extends AbstractController {
    private @Autowired DemoLoyaltyService loyaltyServiceImpl;

    @RequestMapping(path = "/wechat/loyalty/record/purchase", method = RequestMethod.POST)
    public ResponseData recordPurchase(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordPurchase(vo.getId(), vo.getValue(), vo.getRefId(), vo.getEventId())
                .toPrismData();
    }

    @RequestMapping(path = "/wechat/loyalty/record/redeem", method = RequestMethod.POST)
    public ResponseData recordRedeem(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.redeem(vo.getId(), vo.getValue()).toPrismData();
    }

    @RequestMapping(path = "/wechat/loyalty/record/compare", method = RequestMethod.POST)
    public ResponseData recordCompare(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordCustomerEvent("compare", vo.getId()).toPrismData();
    }

    @RequestMapping(path = "/wechat/loyalty/record/detail", method = RequestMethod.POST)
    public ResponseData recordViewDetail(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordCustomerEvent("view-detail", vo.getId()).toPrismData();
    }

    @RequestMapping(path = "/wechat/loyalty/record/like", method = RequestMethod.POST)
    public ResponseData recordLike(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordCustomerEvent("wechat-like", vo.getId()).toPrismData();
    }
    
    @RequestMapping(path = "/wechat/loyalty/record/pv", method = RequestMethod.POST)
    public ResponseData recordPageView(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordCustomerEvent("wechat-pv", vo.getId()).toPrismData();
    }
    
    @RequestMapping(path = "/wechat/loyalty/record/pv1", method = RequestMethod.POST)
    public ResponseData recordPageView1(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordCustomerEvent("wechat-pv-p1", vo.getId()).toPrismData();
    }
    
    @RequestMapping(path = "/wechat/loyalty/record/pv5", method = RequestMethod.POST)
    public ResponseData recordPageView5(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.recordCustomerEvent("wechat-pv-p5", vo.getId()).toPrismData();
    }
    
    @RequestMapping(path = "/wechat/loyalty/user/events", method = RequestMethod.POST)
    public String cutomerEvents(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.getCustomerEvents(vo.getId(), vo.getPageNumber(), vo.getPageSize()).toString();
    }
    
    @RequestMapping(path = "/wechat/loyalty/reward/redeem", method = RequestMethod.POST)
    public ResponseData rewardRedeem(@RequestBody LoyaltyRequestVo vo) throws Exception {
        return loyaltyServiceImpl.redeemReward(vo.getId(), vo.getRewardId()).toPrismData();
    }

    @RequestMapping(path = "/wechat/loyalty/user/info")
    public ResponseData customerInfo(String id) throws Exception {
        return loyaltyServiceImpl.getCustomerInfoFromLoyalty(id).toPrismData();
    }
}
