package com.merkle.wechat.common.dao.digikey.wish;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.wish.WishPerfectLog;

@Repository
public interface WishPerfectLogDao extends PagingAndSortingRepository<WishPerfectLog, Long> {

}
