package com.merkle.wechat.service.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.WechatPublicNoDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.dao.statistics.FollowerStatisticsDao;
import com.merkle.wechat.common.dao.statistics.TotalFollowerStatisticsDao;
import com.merkle.wechat.common.dao.statistics.TotalFollowerStatisticsJobRunLogDao;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.statistics.FollowerStatistics;
import com.merkle.wechat.common.entity.statistics.TotalFollowerStatistics;
import com.merkle.wechat.common.entity.statistics.TotalFollowerStatisticsJobRunLog;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.statistics.FollowerStatisticsItem;
import com.merkle.wechat.vo.statistics.FollowerStatisticsVo;
import com.merkle.wechat.vo.statistics.FollowerStatisticsVo.Item;

@Component
public class FollowerStatisticsServiceImpl implements FollowerStatisticsService {
    protected Logger logger = LoggerFactory.getLogger("FollowerStatisticsService");
    private @Autowired FollowerStatisticsDao followerStatisticsDaoImpl;
    private @Autowired TotalFollowerStatisticsDao totalFollowerStatisticsDaoImpl;
    private @Autowired WechatPublicNoDao wechatPulbicNoDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired TotalFollowerStatisticsJobRunLogDao jobRunLogDaoImpl;

    @Override
    public void createTotalFollowerStatistics() {
        logger.info("===== Start create total follower statistics =====");
        List<WechatPublicNo> pbNos = wechatPulbicNoDaoImpl.findByStatus(WechatPublicNo.ALREADY_AUTH);
        if (pbNos == null) {
            return;
        }
        long currentTime = TimeUtil.currentTimeMillis();
        for (WechatPublicNo pbNo : pbNos) {
            try {
                TotalFollowerStatisticsJobRunLog log = new TotalFollowerStatisticsJobRunLog();
                log.setDateStr(TimeUtil.formatYYYYMMDD(currentTime));
                log.setPbNoId(pbNo.getId());
                jobRunLogDaoImpl.save(log);
            } catch (Exception e) {
                logger.info("===== already run, skip pbNo " + pbNo.getNickName() + " =====");
                continue;
            }
            TotalFollowerStatistics total = totalFollowerStatisticsDaoImpl
                    .findOneByAppIdAndDateStr(pbNo.getAuthorizerAppid(), TimeUtil.formatYYYYMMDD(currentTime));
            total = new TotalFollowerStatistics();
            total.setAppId(pbNo.getAuthorizerAppid());
            total.setWechatPublicNoId(pbNo.getId());
            total.setDateStr(TimeUtil.formatYYYYMMDD(currentTime));
            total.setDateHourStr(TimeUtil.formatYYYYMMDDHH(currentTime));
            total.setTotalCount(followerDaoImpl.countByPubNoAppIdAndSubscribe(pbNo.getAuthorizerAppid(), 1));
            totalFollowerStatisticsDaoImpl.save(total);
        }
        logger.info("===== end create total follower statistics =====");
    }

    @Override
    public void createSubscribe(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            FollowerStatistics s = new FollowerStatistics();
            s.setOpenid(event.getFromUserName());
            s.setDateStr(TimeUtil.formatYYYYMMDD(event.getCreateTime()));
            s.setDateHourStr(TimeUtil.formatYYYYMMDDHH(event.getCreateTime()));
            s.setSubscribe(1);
            s.setUnsubscribe(0);
            s.setAppId(event.getAppId());
            followerStatisticsDaoImpl.save(s);
        });

    }

    @Override
    public void createUnsubscribe(EventMessage event) throws Exception {
        AsyncUtil.asyncRun(() -> {
            FollowerStatistics s = new FollowerStatistics();
            s.setOpenid(event.getFromUserName());
            s.setDateStr(TimeUtil.formatYYYYMMDD(event.getCreateTime()));
            s.setDateHourStr(TimeUtil.formatYYYYMMDDHH(event.getCreateTime()));
            s.setSubscribe(0);
            s.setUnsubscribe(1);
            s.setAppId(event.getAppId());
            followerStatisticsDaoImpl.save(s);
        });
    }

    @Override
    public Map<String, FollowerStatisticsItem> statisticsByDateDetail(WechatPublicNo pbNo, String start, String end,
            int type) {
        if (start.compareTo(end) > 0) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        List<FollowerStatisticsItem> items = statisticsByDate(start, end, type, pbNo.getAuthorizerAppid());
        Map<String, FollowerStatisticsItem> map = new HashMap<>();
        if (items != null) {
            items.forEach((item) -> {
                map.put(item.getDateStr(), item);
            });
        }
        return map;
    }

    private List<FollowerStatisticsItem> statisticsByDate(String start, String end, int type, String appId) {
        List<Object[]> results = null;
        switch (type) {
            // 新增
            case 1:
                results = followerStatisticsDaoImpl.statisticsSubscribeByDate(start, end, appId);
                break;
            // 取消
            case 2:
                results = followerStatisticsDaoImpl.statisticsUnSubscribeByDate(start, end, appId);
                break;
            // 净增
            case 3:
                results = followerStatisticsDaoImpl.statisticsIncreaseByDate(start, end, appId);
                break;
            // total
            case 4:
                results = totalFollowerStatisticsDaoImpl.findByDateAndAppId(start, end, appId);
                break;
            default:
                throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        return convertResultsToItems(results);
    }

    private List<FollowerStatisticsItem> convertResultsToItems(List<Object[]> results) {
        List<FollowerStatisticsItem> vos = new ArrayList<>();
        if (results != null) {
            results.forEach((result) -> {
                vos.add(convertResultToItem(result));
            });
        }
        return vos;
    }

    private FollowerStatisticsItem convertResultToItem(Object[] result) {
        FollowerStatisticsItem item = new FollowerStatisticsItem();
        item.setCount((Long) result[0]);
        item.setDateStr((String) result[1]);
        return item;
    }

    @Override
    public FollowerStatisticsVo statisticsByDate(WechatPublicNo pbNo) {
        FollowerStatisticsVo vo = new FollowerStatisticsVo();
        Item todayItem = vo.getToday();
        Item yesterDayItem = vo.getYesterday();
        String today = TimeUtil.getTodayYYYYMMDD();
        String yesterday = TimeUtil.getYesterdayYYYYMMDD();
        generateItem(todayItem, today, pbNo.getAuthorizerAppid());
        generateItem(yesterDayItem, yesterday, pbNo.getAuthorizerAppid());
        return vo;
    }

    private void generateItem(Item item, String dateStr, String appId) {
        List<Object[]> results = followerStatisticsDaoImpl.statisticsByDate(dateStr, dateStr, appId);
        if (results != null && results.size() > 0) {
            Object[] result = results.get(0);
            item.setSubscribeCount((Long) result[0]);
            item.setUnsubscribeCount((Long) result[1]);
            item.setIncreaseCount((Long) result[2]);
        }
        TotalFollowerStatistics total = totalFollowerStatisticsDaoImpl.findOneByAppIdAndDateStr(appId, dateStr);
        if (total != null) {
            item.setTotalCount(total.getTotalCount());
        } else if (dateStr.equals(TimeUtil.getTodayYYYYMMDD())) {
            item.setTotalCount(followerDaoImpl.countByPubNoAppIdAndSubscribe(appId, 1));
        }
    }

}
