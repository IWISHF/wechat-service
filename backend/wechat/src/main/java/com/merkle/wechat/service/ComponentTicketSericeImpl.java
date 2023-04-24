package com.merkle.wechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.ComponentTicketDao;
import com.merkle.wechat.common.entity.ComponentTicket;

@Component
public class ComponentTicketSericeImpl implements ComponentTicketService {

    private @Autowired ComponentTicketDao componentTicketDaoImpl;

    @Override
    public ComponentTicket findOneByAppId(String thirdPartyAppId) {
        return componentTicketDaoImpl.findFirstByAppId(thirdPartyAppId);
    }

}
