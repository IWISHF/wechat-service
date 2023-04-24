package com.merkle.wechat.modules.digikey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.digikey.video.DigikeyVideoGroupDao;
import com.merkle.wechat.common.entity.digikey.video.DigikeyVideoGroup;
import com.merkle.wechat.vo.Pagination;

@Component
public class DigikeyVideoGroupServiceImpl {
    private @Autowired DigikeyVideoGroupDao digikeyVideoGroupDaoImpl;

    public Pagination<DigikeyVideoGroup> search(Pageable pageable) {
        Page<DigikeyVideoGroup> page = digikeyVideoGroupDaoImpl.findByActive(true, pageable);
        return new Pagination<>(page);
    }
}
