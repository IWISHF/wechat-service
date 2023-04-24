package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.ComponentTicket;

@Repository
public interface ComponentTicketDao extends CrudRepository<ComponentTicket, Long> {
    ComponentTicket findFirstByAppId(String appId);

    ComponentTicket countByAppId(String appId);
}
