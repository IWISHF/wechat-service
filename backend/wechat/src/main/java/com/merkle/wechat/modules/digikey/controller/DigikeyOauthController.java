package com.merkle.wechat.modules.digikey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.digikey.service.DigikeyOAuthServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/wechat/digikey")
@Api(tags = "DIGIKEY OAUTH APIS")
public class DigikeyOauthController extends AbstractController {
    private @Autowired DigikeyOAuthServiceImpl oauthServiceImpl;

    @NeedWrap
    @GetMapping("/oauth/code/request")
    @ApiOperation(value = "request auth code")
    @Deprecated
    public String requestAuthCode(@RequestParam String start) throws Exception {
        if (start.equals("digikeyauthcode")) {
            oauthServiceImpl.requestAuthCode();
            return "ok";
        }
        return "failed";
    }

    @NeedWrap
    @GetMapping("/oauth/code")
    public void digikeyAuthCode(@RequestParam(defaultValue = "") String error, @RequestParam String code)
            throws Exception {
        if (StringUtils.isEmpty(error)) {
            oauthServiceImpl.createAccessToken(code);
        } else {
            throw new Exception(error);
        }
    }
}
