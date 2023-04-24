package com.merkle.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.UserDao;
import com.merkle.wechat.common.dao.UserLoginErrorLogDao;
import com.merkle.wechat.common.entity.User;
import com.merkle.wechat.common.entity.UserLoginErrorLog;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.util.PasswordUtil;
import com.merkle.wechat.vo.UserVo;
import com.merkle.wechat.vo.thridparty.WechatPublicNoVoForLogin;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDaoImpl;
    @Autowired
    private UserLoginErrorLogDao userLoginErrorLogDaoImpl;

    @Override
    public UserVo login(String email, String password) {
        List<UserLoginErrorLog> errorLogs = this.userLoginErrorLogDaoImpl.findByAccountAndTimeBetween(email,
                System.currentTimeMillis() / 1000L - 7200L, System.currentTimeMillis() / 1000L);
        if (errorLogs != null && errorLogs.size() > 7) {
            throw new ServiceWarn(ExceptionConstants.LOGIN_LOCK);
        }
        User user = userDaoImpl.findOneByEmail(email);

        if (user == null) {
            UserLoginErrorLog log = new UserLoginErrorLog();
            log.setAccount(email);
            log.setTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.userLoginErrorLogDaoImpl.save(log);
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }

        if (!PasswordUtil.decryptString(user.getPassword()).equals(password)) {
            UserLoginErrorLog log = new UserLoginErrorLog();
            log.setAccount(email);
            log.setTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.userLoginErrorLogDaoImpl.save(log);
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        List<WechatPublicNo> pbNos = user.getPbNos().stream().filter((p) -> {
            return p.getStatus().equals(WechatPublicNo.ALREADY_AUTH);
        }).collect(Collectors.toList());
        List<WechatPublicNoVoForLogin> vos = new ArrayList<>();
        if (pbNos != null) {
            pbNos.forEach((pbNo) -> {
                WechatPublicNoVoForLogin vo = new WechatPublicNoVoForLogin();
                BeanUtils.copyProperties(pbNo, vo);
                vos.add(vo);
            });
        }
        userVo.setPbNos(vos);
        return userVo;
    }

    @Override
    public void updatePassword(String userId, String oldPassword, String newPassword) {
        User user = userDaoImpl.findOne(Long.valueOf(userId));
        if (user == null) {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }

        if (!PasswordUtil.decryptString(user.getPassword()).equals(oldPassword)) {
            throw new ServiceWarn(ExceptionConstants.PASSWORD_ERROR);
        }

        user.setPassword(PasswordUtil.encryptString(newPassword));
        userDaoImpl.save(user);
    }
}
