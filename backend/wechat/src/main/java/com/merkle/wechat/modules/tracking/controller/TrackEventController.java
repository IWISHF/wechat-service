package com.merkle.wechat.modules.tracking.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.tracking.TrackCampaignPageEvent;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.modules.tracking.service.TrackCampaignPageEventService;

@Controller
@RequestMapping("/wechat/track")
public class TrackEventController extends AbstractController {
    private @Autowired TrackCampaignPageEventService trackCPEServiceImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;

    @NeedWrap
    @PostMapping("/page/event")
    public String trackCPEEvent(@RequestBody @Valid TrackCampaignPageEvent event, HttpServletRequest request)
            throws Exception {
        event.setHost(request.getRemoteAddr());
        event.setxForwardFor(request.getHeader("x-forwarded-for"));
        event.setUserAgent(request.getHeader("user-agent"));
        TrackCampaignPageEvent e = trackCPEServiceImpl.saveCPE(event);
        if (event.getChannelType().toLowerCase().contains("wechat")) {
            AsyncUtil.asyncRun(() -> {
                loyaltyServiceImpl.recordCPE(e);
            });
        }
        return "ok";
    }
}
