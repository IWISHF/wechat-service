package com.merkle.wechat.modules.digikey.controller;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.dao.digikey.CampaignDataDao;
import com.merkle.wechat.common.dao.digikey.CampaignShareLogDao;
import com.merkle.wechat.common.dao.digikey.DigikeyEventDao;
import com.merkle.wechat.common.entity.digikey.CampaignData;
import com.merkle.wechat.common.entity.digikey.CampaignShareLog;
import com.merkle.wechat.common.entity.digikey.DigikeyEvent;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.vo.CampaignDataVo;
import com.merkle.wechat.service.follower.FollowerService;

@Controller
@RequestMapping("/wechat/digikey")
public class AnnualCampaginController extends AbstractController {
    private @Autowired CampaignDataDao campaignDataDao;
    private @Autowired FollowerService followerServieImpl;
    private @Autowired CampaignShareLogDao campaignShareLogDao;
    private @Autowired DigikeyEventDao digikeyEventDao;

    @NeedWrap
    @PostMapping("/campaign")
    public String saveCampaignData(@RequestBody @Valid CampaignDataVo vo) throws Exception {
        CampaignData data = campaignDataDao.findOneByOpenid(vo.getOpenid());
        data = Optional.ofNullable(data).orElseGet(() -> new CampaignData());
        BeanUtils.copyProperties(data, vo);
        campaignDataDao.save(data);
        return "success";
    }

    @NeedWrap
    @GetMapping("/campaign/{openid}")
    public CampaignDataVo getCampaignData(@PathVariable String openid) throws Exception {
        CampaignData data = campaignDataDao.findOneByOpenid(openid);
        data = Optional.ofNullable(data).orElseThrow(() -> new ServiceWarn("not exist!"));
        CampaignDataVo vo = new CampaignDataVo();
        BeanUtils.copyProperties(vo, data);
        return vo;
    }

    @NeedWrap
    @GetMapping("/follower/check/{openid}")
    public boolean check(@PathVariable String openid) throws Exception {
        Optional<Follower> follower = Optional.ofNullable(followerServieImpl.findOneByOpenid(openid));
        if (follower.isPresent() && follower.get().getSubscribe().intValue() != 0) {
            return true;
        }
        return false;
    }

    @NeedWrap
    @GetMapping("/campaign/{openid}/shared")
    public String recordShare(@PathVariable String openid, String type) {
        CampaignData data = campaignDataDao.findOneByOpenid(openid);
        data = Optional.ofNullable(data).orElseThrow(() -> new ServiceWarn("not exist!"));
        data.setUpdated(new Date());
        data.setShared(true);
        data.setShareCount(data.getShareCount() + 1);
        campaignDataDao.save(data);
        CampaignShareLog log = new CampaignShareLog();
        log.setOpenid(openid);
        log.setType(type);
        campaignShareLogDao.save(log);
        return "success";
    }

    @NeedWrap
    @GetMapping("/campaign/event/{openid}")
    public String recordPageViewEvent(@PathVariable String openid, String type, String content) {
        DigikeyEvent event = new DigikeyEvent();
        event.setEvent(type);
        event.setOpenid(openid);
        event.setContent(content);
        digikeyEventDao.save(event);
        return "success";
    }
}
