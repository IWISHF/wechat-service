package com.merkle.wechat.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.security.Role;
import com.merkle.wechat.security.UserInfo;
import com.merkle.wechat.service.UserService;
import com.merkle.wechat.util.JwtUtil;
import com.merkle.wechat.vo.UpdateUserVo;
import com.merkle.wechat.vo.UserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = { "User管理" })
@RequestMapping("/wechat/user")
public class UserController extends AbstractController {
    private @Autowired UserService userServiceImpl;

    @NeedWrap
    @PostMapping("/login")
    @ApiOperation("登陆")
    public UserVo login(@Valid @NotEmpty String password, @Valid @NotEmpty String email, HttpServletResponse response)
            throws Exception {
        UserVo vo = userServiceImpl.login(email, password);
        String authToken = JwtUtil.generateToken(new UserInfo(vo.getId() + "",
                vo.getPbNos().stream().map((pbNo) -> pbNo.getId() + "").collect(Collectors.joining(",")),
                Role.SA.toString()));
        response.setHeader("x-auth-token", authToken);
        response.setHeader("Access-Control-Expose-Headers", "x-auth-token");
        return vo;
    }

    @NeedWrap
    @PutMapping("/pwd")
    @ApiOperation("修改密码")
    public String updatePwd(@Valid @RequestBody UpdateUserVo vo) throws Exception {
        UserInfo userInfo = retrieveTokenUserInfo();
        String userId = userInfo.getUserId();
        userServiceImpl.updatePassword(userId, vo.getOldPassword(), vo.getNewPassword());
        return "ok";
    }

}
