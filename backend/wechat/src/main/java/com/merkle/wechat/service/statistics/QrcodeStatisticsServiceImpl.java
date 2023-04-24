package com.merkle.wechat.service.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.QrcodeDao;
import com.merkle.wechat.common.dao.statistics.QrcodeStatisticsDao;
import com.merkle.wechat.common.entity.EventMessage;
import com.merkle.wechat.common.entity.Qrcode;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.statistics.QrcodeStatistics;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.statistics.QrcodeStatisticsItem;
import com.merkle.wechat.vo.statistics.QrcodeStatisticsVo;

@Component
public class QrcodeStatisticsServiceImpl implements QrcodeStatisticsService {
    private @Autowired QrcodeStatisticsDao qrcodeStatisticsDaoImpl;
    private @Autowired QrcodeDao qrcodeDaoImpl;

    @Override
    public void create(EventMessage event, Qrcode qr, boolean isSubscribe) {
        QrcodeStatistics s = new QrcodeStatistics();
        s.setQrcodeId(qr.getId());
        s.setOpenid(event.getFromUserName());
        s.setScanDate(TimeUtil.formatYYYYMMDD(event.getCreateTime()));
        s.setScanDateHour(TimeUtil.formatYYYYMMDDHH(event.getCreateTime()));
        s.setIsSubscribe(isSubscribe ? 1 : 0);
        qrcodeStatisticsDaoImpl.save(s);
    }

    @Override
    public List<QrcodeStatisticsItem> statisticsByDate(String start, String end, Long qrcodeId) {
        List<Object[]> results = qrcodeStatisticsDaoImpl.statisticsByDate(start, end, qrcodeId);
        return convertResultsToItems(results);
    }

    private List<QrcodeStatisticsItem> convertResultsToItems(List<Object[]> results) {
        List<QrcodeStatisticsItem> vos = new ArrayList<>();
        if (results != null) {
            results.forEach((result) -> {
                vos.add(convertResultToItem(result));
            });
        }
        return vos;
    }

    private QrcodeStatisticsItem convertResultToItem(Object[] result) {
        QrcodeStatisticsItem item = new QrcodeStatisticsItem();
        item.setScanCount((Long) result[0]);
        item.setSubscribeCount((Long) result[1]);
        item.setQrcodeId((Long) result[2]);
        item.setDateStr((String) result[3]);
        return item;
    }

    @Override
    public Pagination<QrcodeStatisticsVo> searchByName(WechatPublicNo pbNo, String key, Pageable pageable) {
        Page<Qrcode> qrcodes = qrcodeDaoImpl.findByNameContainingAndWechatPublicNoId(key, pbNo.getId(), pageable);
        List<Qrcode> content = qrcodes.getContent();
        List<QrcodeStatisticsVo> vos = convertQrcodeToQrcodeStatisticsVo(content);
        Pagination<QrcodeStatisticsVo> pagination = new Pagination<>();
        BeanUtils.copyProperties(new Pagination<Qrcode>(qrcodes), pagination, "result");
        pagination.setResult(vos);
        return pagination;
    }

    private List<QrcodeStatisticsVo> convertQrcodeToQrcodeStatisticsVo(List<Qrcode> content) {
        List<QrcodeStatisticsVo> vos = new ArrayList<>();
        if (content != null) {
            content.forEach((qr) -> {
                vos.add(convertQrcodeToQrcodeStatisticsVo(qr));
            });
        }
        return vos;
    }

    private QrcodeStatisticsVo convertQrcodeToQrcodeStatisticsVo(Qrcode qr) {
        QrcodeStatisticsVo vo = new QrcodeStatisticsVo();
        vo.setQrcodeId(qr.getId());
        vo.setQrcodeName(qr.getName());
        vo.setTotalScan(qrcodeStatisticsDaoImpl.countByQrcodeId(qr.getId()));
        vo.setTotalSubscribe(qrcodeStatisticsDaoImpl.countByQrcodeIdAndIsSubscribe(qr.getId(), 1));

        String yesterday = TimeUtil.getYesterdayYYYYMMDD();
        vo.setYesterdayScanCount(0L);
        vo.setYesterdaySubscribeCount(0L);
        List<Object[]> results = qrcodeStatisticsDaoImpl.statisticsByDate(yesterday, yesterday, qr.getId());
        if (results.size() != 0) {
            QrcodeStatisticsItem item = convertResultToItem(results.get(0));
            vo.setYesterdayScanCount(item.getScanCount());
            vo.setYesterdaySubscribeCount(item.getSubscribeCount());
        }
        return vo;
    }

    @Override
    public Map<String, QrcodeStatisticsItem> statisticsByDateDetail(WechatPublicNo pbNo, String start, String end,
            Long qrcodeId) {
        if (start.compareTo(end) > 0) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        List<QrcodeStatisticsItem> items = statisticsByDate(start, end, qrcodeId);
        Map<String, QrcodeStatisticsItem> map = new HashMap<>();
        if (items != null) {
            items.forEach((item) -> {
                map.put(item.getDateStr(), item);
            });
        }
        return map;
    }
}
