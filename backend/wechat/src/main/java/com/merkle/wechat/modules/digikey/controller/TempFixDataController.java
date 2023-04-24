package com.merkle.wechat.modules.digikey.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeySyncUnionidServiceImpl;

@Controller
@RequestMapping("/wechat/digikey")
@Deprecated
public class TempFixDataController extends AbstractController {
    private @Autowired DigikeySyncUnionidServiceImpl digikeySyncUnionidServiceImpl;

    // @NeedWrap
    // @GetMapping("/fix/bindinfo")
    // public boolean fixBindInfo(@Valid @NotEmpty String start) throws
    // Exception {
    // if (start.equals("bindfix")) {
    // fixServiceImpl.fixBindInfo();
    // } else if (start.equals("campaignFix")) {
    // fixServiceImpl.fixCampaign();
    // }
    // return true;
    // }

    @NeedWrap
    @GetMapping("/fix/unionid")
    public boolean fixBindInfo(@Valid @NotEmpty String start, String appId) throws Exception {
        if (start.equals("unionid")) {
            AsyncUtil.asyncRun(() -> {
                try {
                    digikeySyncUnionidServiceImpl.updateExternalCustomerId(appId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return true;
    }
}
