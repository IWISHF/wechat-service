package com.merkle.wechat.modules.digikey.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.dao.digikey.DigikeyEventMultiChannelDao;
import com.merkle.wechat.common.entity.digikey.DigikeyEventMultiChannel;
import com.merkle.wechat.common.entity.digikey.DigikeyLikeOrUnlikeRecord;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyService;
import com.merkle.wechat.modules.digikey.vo.LikeOrUnlikeStatisticsVo;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;

@Controller
@RequestMapping("/wechat/digikey")
public class DigikeyEventController extends AbstractController {
    private @Autowired DigikeyEventMultiChannelDao digikeyEventTempDaoImpl;
    private @Autowired DigikeyService digikeyServiceImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;

    @NeedWrap
    @PostMapping("/event")
    public String save(@RequestBody DigikeyEventMultiChannel event, HttpServletRequest request) throws Exception {
        event.setHost(request.getRemoteAddr());
        event.setUserAgent(request.getHeader("user-agent"));
        digikeyEventTempDaoImpl.save(event);
        // TODO: currently only record wechat events
        if (event.getChannelType().toLowerCase().contains("wechat")) {
            AsyncUtil.asyncRun(() -> {
                loyaltyServiceImpl.recordDigikeyEvent(event);
            });
        }
        return "ok";
    }

    @NeedWrap
    @PostMapping("/like/vote")
    public LikeOrUnlikeStatisticsVo saveLikeOrUnlikeRecord(@RequestBody DigikeyLikeOrUnlikeRecord record,
            HttpServletRequest request) throws Exception {
        record.setHost(request.getRemoteAddr());
        record.setUserAgent(request.getHeader("user-agent"));
        return digikeyServiceImpl.saveLikeOrUnlikeRecord(record);
    }

    @NeedWrap
    @GetMapping("/like/info")
    public LikeOrUnlikeStatisticsVo getLikeInfo(String identityId, String pageAlias) throws Exception {
        return digikeyServiceImpl.getLikeInfo(identityId, pageAlias);
    }

}
