package com.merkle.wechat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.RewardsRedeemLogDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.follower.TicketCheckVo;

@Component
public class RewardsRedeemLogServiceImpl implements RewardsRedeemLogService {
    private @Autowired RewardsRedeemLogDao rewardsRedeemLogDaoImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired FollowerDao followerDaoImpl;

    @Override
    public RewardsRedeemLog save(RewardsRedeemLog redeemLog) {
        Follower f = followerDaoImpl.findOneByOpenid(redeemLog.getOpenid());
        redeemLog.setUnionid(f.getUnionid());
        return rewardsRedeemLogDaoImpl.save(redeemLog);
    }

    @Override
    public Pagination<RewardsRedeemLog> search(Long channelId, String key, String type, Pageable pageable)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        Page<RewardsRedeemLog> logs = rewardsRedeemLogDaoImpl.findByConditions(pbNo.getAuthorizerAppid(), key, type,
                pageable);
        return new Pagination<>(logs);
    }

    @Override
    public void checkin(TicketCheckVo vo) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(vo.getChannelId());
        Follower f = followerDaoImpl.findOneByOpenid(vo.getOpenid());
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            RewardsRedeemLog log = Optional
                    .ofNullable(rewardsRedeemLogDaoImpl.findOneByUnionidAndRewardIdAndCouponCode(f.getUnionid(),
                            vo.getRewardId(), vo.getCode()))
                    .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));

            if (log.isCheckStatus()) {
                throw new ServiceWarn(ExceptionConstants.ALREADY_USED);
            }

            log.setCheckStatus(true);
            rewardsRedeemLogDaoImpl.save(log);

        } else {
            RewardsRedeemLog log = Optional
                    .ofNullable(rewardsRedeemLogDaoImpl.findByPubNoAppIdAndOpenidAndRewardIdAndCouponCode(
                            pbNo.getAuthorizerAppid(), vo.getOpenid(), vo.getRewardId(), vo.getCode()))
                    .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));

            if (log.isCheckStatus()) {
                throw new ServiceWarn(ExceptionConstants.ALREADY_USED);
            }

            log.setCheckStatus(true);
            rewardsRedeemLogDaoImpl.save(log);
        }
    }

    @Override
    public void checkTicketStatus(TicketCheckVo vo) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(vo.getChannelId());
        Follower f = followerDaoImpl.findOneByOpenid(vo.getOpenid());
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            RewardsRedeemLog log = Optional
                    .ofNullable(rewardsRedeemLogDaoImpl.findOneByUnionidAndRewardIdAndCouponCode(f.getUnionid(),
                            vo.getRewardId(), vo.getCode()))
                    .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));

            if (log.isCheckStatus()) {
                throw new ServiceWarn(ExceptionConstants.ALREADY_USED);
            }
        } else {
            RewardsRedeemLog log = Optional
                    .ofNullable(rewardsRedeemLogDaoImpl.findByPubNoAppIdAndOpenidAndRewardIdAndCouponCode(
                            pbNo.getAuthorizerAppid(), vo.getOpenid(), vo.getRewardId(), vo.getCode()))
                    .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));

            if (log.isCheckStatus()) {
                throw new ServiceWarn(ExceptionConstants.ALREADY_USED);
            }
        }

    }

    @Override
    public List<RewardsRedeemLog> getAllRewards(String openid, Long channelId) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        Follower f = followerDaoImpl.findOneByOpenid(openid);
        if (f != null && !StringUtils.isEmpty(f.getUnionid())) {
            return rewardsRedeemLogDaoImpl.findByUnionidAndRedeemStatusOrderByIdDesc(f.getUnionid(), true);
        }
        return rewardsRedeemLogDaoImpl.findByOpenidAndPubNoAppIdAndRedeemStatusOrderByIdDesc(openid,
                pbNo.getAuthorizerAppid(), true);
    }

}
