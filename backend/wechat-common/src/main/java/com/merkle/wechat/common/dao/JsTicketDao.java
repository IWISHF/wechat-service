package com.merkle.wechat.common.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.JsTicket;

@Repository
public interface JsTicketDao extends CrudRepository<JsTicket, Long> {
    JsTicket findByAppId(String appId);

    List<JsTicket> findByCreatedDateLessThan(Date date);
}
