package com.merkle.wechat.common.dao.digikey.video;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.video.DigikeyVideo;

@Repository
public interface DigikeyVideoDao extends PagingAndSortingRepository<DigikeyVideo, Long> {

    Page<DigikeyVideo> findByNameContaining(String key, Pageable pageable);

    Page<DigikeyVideo> findByNameContainingAndGroup_idIn(String key, List<Long> groupIds, Pageable pageable);

    Page<DigikeyVideo> findByNameContainingAndActive(String key, boolean b, Pageable pageable);

    Page<DigikeyVideo> findByNameContainingAndGroup_idInAndActive(String key, List<Long> groupIds, boolean b,
            Pageable pageable);

}
