package com.merkle.wechat.common.dao.digikey;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.DigikeyToken;

@Repository
public interface DigikeyTokenDao extends CrudRepository<DigikeyToken, Long> {
    DigikeyToken findByClientId(String clientId);

    List<DigikeyToken> findByCreatedDateLessThan(Date date);
}
