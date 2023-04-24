package com.merkle.wechat.common.dao.digikey.video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.video.DigikeyVideoGroup;

@Repository
public interface DigikeyVideoGroupDao extends CrudRepository<DigikeyVideoGroup, Long> {
    Page<DigikeyVideoGroup> findByActive(boolean active, Pageable pageable);
}
