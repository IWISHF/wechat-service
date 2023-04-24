package com.merkle.wechat.modules.digikey.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.digikey.TemplateMessageTaskItems;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.TemplateMessageServiceImpl;

import weixin.popular.bean.message.PrivateTemplate;

@Controller
@RequestMapping("/wechat/digikey/templatemessage")
public class TemplateMessageController extends AbstractController {
    private @Autowired TemplateMessageServiceImpl templateMessageServiceImpl;

    @NeedWrap
    @GetMapping("/all")
    public List<PrivateTemplate> getAllTemplate() {
        return templateMessageServiceImpl.getAllTemplate();
    }

    @NeedWrap
    @PostMapping("/send")
    public void sendTemplate(@RequestBody List<String> toUsers, HttpServletResponse response) throws Exception {
        AsyncUtil.asyncRun(() -> {
            toUsers.forEach((toUser) -> {
                templateMessageServiceImpl.sendTemplateMessage(toUser);
            });
        });
    }

    @NeedWrap
    @GetMapping("/send/qq/vip")
    public void sendTemplateWithQQVipCode(String start, HttpServletResponse response) throws Exception {
        if (start.equals("qq")) {
            AsyncUtil.asyncRun(() -> {
                templateMessageServiceImpl.sendTemplateMessageWithQQVip();
            });
        }
    }

    @NeedWrap
    @GetMapping("/send/qq/vip/preview")
    public void sendTemplateWithQQVipCodePreview(String start, String touser, String code) throws Exception {
        if (start.equals("qqpreview")) {
            AsyncUtil.asyncRun(() -> {
                templateMessageServiceImpl.sendTemplateMessageWithQQVipPreview(touser, code);
            });
        }
    }

    @NeedWrap
    @GetMapping("/send/common")
    public String commonSendTemplate(String start, Long taskId) throws Exception {
        if (start.equals("common")) {
            AsyncUtil.asyncRun(() -> {
                templateMessageServiceImpl.commonSendTemplateMessage(taskId);
            });
        }
        return "ok";
    }

    @NeedWrap
    @PostMapping("/send/common/preview")
    public String commonSendTemplatePreview(String start, @RequestBody TemplateMessageTaskItems item) throws Exception {
        if (start.equals("commonPreview")) {
            AsyncUtil.asyncRun(() -> {
                templateMessageServiceImpl.commonSendTemplateMessagePreview(item);
            });
        }

        return "ok";
    }
}
