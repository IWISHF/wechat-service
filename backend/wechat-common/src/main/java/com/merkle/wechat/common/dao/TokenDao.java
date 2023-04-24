package com.merkle.wechat.common.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.Token;

@Repository
public interface TokenDao extends CrudRepository<Token, Long> {

    Token findByAppId(String thirdPartyAppId);

    List<Token> findByCreatedDateLessThan(Date date);

}
