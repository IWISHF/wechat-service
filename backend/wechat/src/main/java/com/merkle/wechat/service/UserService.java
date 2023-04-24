package com.merkle.wechat.service;

import com.merkle.wechat.vo.UserVo;

public interface UserService {

    UserVo login(String email, String password);

    void updatePassword(String userId, String oldPassword, String newPassword);

}
