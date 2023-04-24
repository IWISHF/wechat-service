package com.merkle.wechat.service;

import com.merkle.wechat.common.entity.JsTicket;

public interface JsTicketService {

    String getJsTicket(String appId);

    JsTicket createOrUpdateJsTicketFromWechat(String appId);

    void refreshJsTickets();

}
