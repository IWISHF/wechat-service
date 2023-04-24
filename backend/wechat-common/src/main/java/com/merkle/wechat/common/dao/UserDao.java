package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, Long> {

    User findOneByPasswordAndEmail(String pwd, String email);

    User findOneByEmail(String email);

}
