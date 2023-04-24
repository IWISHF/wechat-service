package com.merkle.wechat.service.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.statistics.TagStatisticsVo;

@Component
public class TagStatisticsServiceImpl implements TagStatisticsService {
    private @Autowired TagDao tagDaoImpl;

    @Override
    public TagStatisticsVo searchTagByName(WechatPublicNo pbNo, String key, int fromWechat, Pageable pageable) {
        Page<Tag> page;
        if (fromWechat == -1) {
            page = tagDaoImpl.findByNameContainingAndWechatPublicNoIdOrderByCountDesc(key, pbNo.getId(), pageable);
        } else {

            page = tagDaoImpl.findByNameAndWechatPublicNoIdAndFromWechatOrderByCountDesc(key, pbNo.getId(),
                    fromWechat == 1 ? true : false, pageable);
        }
        return new TagStatisticsVo(page);
    }

}
