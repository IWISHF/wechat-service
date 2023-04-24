package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.follower.TicketCheckVo;

public interface RewardsRedeemLogService {

    RewardsRedeemLog save(RewardsRedeemLog redeemLog);

    Pagination<RewardsRedeemLog> search(Long channelId, String key, String type, Pageable pageable) throws Exception;

    void checkin(TicketCheckVo vo) throws Exception;

    void checkTicketStatus(TicketCheckVo vo) throws Exception;

    List<RewardsRedeemLog> getAllRewards(String openid, Long channelId) throws Exception;

}
