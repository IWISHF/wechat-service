package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.cache.CacheTemplate;
import com.merkle.wechat.common.dao.JsTicketDao;
import com.merkle.wechat.common.dao.RefreshFlagDao;
import com.merkle.wechat.common.entity.JsTicket;
import com.merkle.wechat.common.entity.RefreshFlag;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.Constants;

import weixin.popular.api.TicketAPI;
import weixin.popular.bean.ticket.Ticket;

@Component
public class JsTicketServiceImpl implements JsTicketService {
    protected Logger logger = LoggerFactory.getLogger("JsTicketServiceImpl");

    private @Autowired CacheTemplate stringCacheTemplate;

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired JsTicketDao jsTicketDao;

    private @Autowired RefreshFlagDao refreshFlagDao;

    private final String JS_TICKET_PREFIX = "js_ticket_";

    @Override
    public String getJsTicket(String appId) {
        String jsTicketStr = stringCacheTemplate.get(JS_TICKET_PREFIX + appId);

        if (StringUtils.isNotEmpty(jsTicketStr)) {
            Long expireDate = Long
                    .valueOf(stringCacheTemplate.get(JS_TICKET_PREFIX + appId + Constants.EXPIRED_DATE_SUFFIX));
            if (System.currentTimeMillis() >= expireDate) {
                jsTicketStr = syncDataBaseTicketToCache(appId);
            }
        } else {
            jsTicketStr = syncDataBaseTicketToCache(appId);
        }
        return jsTicketStr;
    }

    private String syncDataBaseTicketToCache(String appId) {
        JsTicket ticket = jsTicketDao.findByAppId(appId);

        if (ticket == null) {
            ticket = createOrUpdateJsTicketFromWechat(appId);
        }

        String ticketStr = ticket.getTicket();
        putTicketToCache(appId, ticket);
        return ticketStr;
    }

    private void putTicketToCache(String appId, JsTicket ticket) {
        stringCacheTemplate.put(JS_TICKET_PREFIX + appId, ticket.getTicket());
        long expired = ticket.getCreatedDate().getTime() + 88 * 60 * 1000;
        stringCacheTemplate.put(JS_TICKET_PREFIX + appId + Constants.EXPIRED_DATE_SUFFIX, expired + "");
    }

    @Override
    public JsTicket createOrUpdateJsTicketFromWechat(String appId) {
        String accessToken = tokenServiceImpl.getPublicNoAccessTokenByAppId(appId);
        Ticket ticket = TicketAPI.ticketGetticket(accessToken);
        if (ticket.isSuccess()) {
            JsTicket dbTicket = jsTicketDao.findByAppId(appId);
            if (dbTicket == null) {
                dbTicket = new JsTicket();
            }
            dbTicket.setAppId(appId);
            dbTicket.setCreatedDate(new Date());
            dbTicket.setTicket(ticket.getTicket());
            dbTicket = jsTicketDao.save(dbTicket);
            return dbTicket;
        } else {
            throw new ServiceWarn("Get jsTicket from wechat failed!");
        }
    }

    @Override
    public void refreshJsTickets() {
        logger.info("============= refresh jsapi ticket Start ======");
        List<JsTicket> tickets = getNearExpireTickets();

        for (JsTicket ticket : tickets) {
            refreshTicket(ticket);
        }
        logger.info("============= refresh jsapi ticket end======");
    }

    private void refreshTicket(JsTicket ticket) {
        String appId = ticket.getAppId();
        createJsTicketRefreshFlag(appId);
        String accessToken = tokenServiceImpl.getPublicNoAccessTokenByAppId(appId);
        Ticket newTicket = TicketAPI.ticketGetticket(accessToken);
        if (newTicket.isSuccess()) {
            JsTicket dbTicket = jsTicketDao.findByAppId(appId);
            if (dbTicket == null) {
                dbTicket = new JsTicket();
            }
            dbTicket.setAppId(appId);
            dbTicket.setCreatedDate(new Date());
            dbTicket.setTicket(newTicket.getTicket());
            dbTicket = jsTicketDao.save(dbTicket);
            putTicketToCache(appId, dbTicket);
        }
        removeJsTicketRefreshFlag(appId);
    }

    private void removeJsTicketRefreshFlag(String appId) {
        try {
            refreshFlagDao.removeByAppId(JS_TICKET_PREFIX + appId);
        } catch (Exception e) {
            logger.info("delete refresh flag exception:" + e.getMessage());
        }
    }

    private void createJsTicketRefreshFlag(String appId) {
        try {
            refreshFlagDao.save(new RefreshFlag(JS_TICKET_PREFIX + appId));
        } catch (Exception e) {
            // 1 hour and 40 minutes ago
            // protect jsTicket can be refresh successfully
            Date date = new Date(System.currentTimeMillis() - 600000);
            RefreshFlag flag = refreshFlagDao.findByAppIdAndCreatedDateLessThan(JS_TICKET_PREFIX + appId, date);
            if (null != flag) {
                refreshFlagDao.delete(flag);
            }
        }
    }

    public List<JsTicket> getNearExpireTickets(Date date) {
        return jsTicketDao.findByCreatedDateLessThan(date);
    }

    public List<JsTicket> getNearExpireTickets() {
        long currentTimeMills = System.currentTimeMillis();
        // 1 h 30 minutes
        currentTimeMills -= 90 * 60 * 1000;
        Date date = new Date(currentTimeMills);
        return jsTicketDao.findByCreatedDateLessThan(date);
    }

}
