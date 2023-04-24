package com.merkle.wechat.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.service.OAuthService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.vo.thridparty.JsTicketConfigVo;

import io.swagger.annotations.Api;

@Controller
@Api(tags = "Oauth验证")
@RequestMapping("/wechat")
public class OAuthController extends AbstractController {

    private @Autowired OAuthService oauthServiceImpl;
    private @Autowired FollowerService followerServiceImpl;

    @GetMapping("/{channel}/oauth")
    public void oauth(@PathVariable Long channel, HttpServletResponse response, String redirect,
            HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuffer sb = new StringBuffer();
        sb.append(redirect);

        parameterMap.forEach((key, value) -> {
            if (!key.equals("channel") && !key.equals("redirect") && !key.equals("from")
                    && !key.equals("isappinstalled")) {
                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(Arrays.asList(value).stream().collect(Collectors.joining(",")));
            }
        });
        redirect = sb.toString();
        response.sendRedirect(oauthServiceImpl.generateOauthUrl(channel, redirect));
    }

    @GetMapping("/oauth/code")
    public void receiveCode(String base64path, String code, String state, HttpServletResponse response)
            throws Exception {
        response.sendRedirect(oauthServiceImpl.generateRedirectUrl(base64path, code, state));
    }

    @NeedWrap
    @GetMapping("/{channel}/jssdk")
    public JsTicketConfigVo wxJssdkSignature(@PathVariable Long channel, String url) {
        return oauthServiceImpl.generateJsTicketConfig(channel, url);
    }

    @NeedWrap
    @GetMapping("/follower/check/{openid}")
    public boolean check(@PathVariable String openid) throws Exception {
        Optional<Follower> follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid));
        if (follower.isPresent() && follower.get().getSubscribe().intValue() != 0) {
            return true;
        }
        return false;
    }

}
