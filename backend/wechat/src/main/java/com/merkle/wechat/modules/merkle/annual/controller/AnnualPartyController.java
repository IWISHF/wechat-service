package com.merkle.wechat.modules.merkle.annual.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.merkle.annual.service.AnnualVoteServiceImpl;
import com.merkle.wechat.modules.merkle.annual.vo.VoteSummaryVo;
import com.merkle.wechat.modules.merkle.annual.vo.VoteVo;

@RestController
@RequestMapping("/wechat")
public class AnnualPartyController extends AbstractController {
    private @Autowired AnnualVoteServiceImpl voteService;
    
    @NeedWrap
    @RequestMapping(path = "/annual/vote/check", method = RequestMethod.GET)
    public VoteVo validVote(String openid) {
        return voteService.valid(openid);
    }
    
    @NeedWrap
    @RequestMapping(path = "/annual/vote", method = RequestMethod.POST)
    public String vote(@RequestBody VoteVo vo) {
        if(!voteService.enableVote()) {
            throw new ServiceWarn("vote not enable!", 1102);
        }
        String voteStr = vo.getVoteStr();
        if (StringUtils.isEmpty(voteStr) || voteStr.length() != VoteSummaryVo.LENGTH || voteStr.split("1").length > 4
                || StringUtils.isEmpty(vo.getOpenid())) {
            throw new ServiceWarn("params error!", 1101);
        }
        voteService.save(vo);
        return "success";
    }
    
    @NeedWrap
    @RequestMapping(path = "/annual/summary", method = RequestMethod.GET)
    public VoteSummaryVo voteSummary() {
        return voteService.summary();
    }
    
    @NeedWrap
    @RequestMapping(path = "/annual/vote/enable", method = RequestMethod.GET)
    public String enableAnnual(String type) {
        voteService.enableOrDisableVote(type);
        return "success";
    }
    
}
