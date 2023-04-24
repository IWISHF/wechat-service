package com.merkle.wechat.service;

import com.merkle.wechat.common.entity.ComponentTicket;

public interface ComponentTicketService {

    ComponentTicket findOneByAppId(String thirdPartyAppId);

}
