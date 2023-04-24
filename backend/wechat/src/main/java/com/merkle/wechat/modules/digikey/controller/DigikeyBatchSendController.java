package com.merkle.wechat.modules.digikey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyBatchSendServiceImpl;

@Controller
@RequestMapping("/wechat/digikey")
public class DigikeyBatchSendController extends AbstractController {

    private @Autowired DigikeyBatchSendServiceImpl digikeyBatchSendServiceImpl;

    @NeedWrap
    @RequestMapping("/batch/munihei")
    public String send(String start) {
        if (start != null && start.equals("dianzizhan")) {
            digikeyBatchSendServiceImpl.send();
        }
        return "ok";
    }

    @NeedWrap
    @RequestMapping("/batch/munihei/preview")
    public String sendPreview(String start,
            @RequestParam(defaultValue = "oRGqIt0xsnNfNZJcWgkuC4XDWXGA") String openid) {
        if (start != null && start.equals("dianzizhan")) {
            digikeyBatchSendServiceImpl.sendPreview(openid);
        }
        return "ok";
    }

    @NeedWrap
    @RequestMapping("/batch/emi/send")
    public String sendEMI(String start) {
        if (start != null && start.equals("emi")) {
            digikeyBatchSendServiceImpl.sendEMIArticle();
        }
        return "ok";
    }
}
