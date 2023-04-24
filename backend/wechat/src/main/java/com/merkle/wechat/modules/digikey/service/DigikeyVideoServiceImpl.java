package com.merkle.wechat.modules.digikey.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.digikey.video.DigikeyVideoDao;
import com.merkle.wechat.common.entity.digikey.video.DigikeyVideo;
import com.merkle.wechat.vo.Pagination;

@Component
public class DigikeyVideoServiceImpl {
    private @Autowired DigikeyVideoDao digikeyVideoDaoImpl;

    public Pagination<DigikeyVideo> search(Pageable pageable, String key, String groups) {
        if (StringUtils.isEmpty(groups)) {
            Page<DigikeyVideo> page = digikeyVideoDaoImpl.findByNameContainingAndActive(key, true, pageable);
            return new Pagination<>(page);
        } else {
            String[] groupStrs = groups.split(",");
            List<Long> groupIds = new ArrayList<>();
            for (String groupStr : groupStrs) {
                groupIds.add(Long.parseLong(groupStr));
            }
            Page<DigikeyVideo> page = digikeyVideoDaoImpl.findByNameContainingAndGroup_idInAndActive(key, groupIds,
                    true, pageable);
            return new Pagination<>(page);
        }
    }

    public DigikeyVideo findById(Long id) {
        return digikeyVideoDaoImpl.findOne(id);
    }
}
