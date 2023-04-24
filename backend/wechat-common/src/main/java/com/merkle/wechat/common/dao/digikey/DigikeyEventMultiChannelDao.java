package com.merkle.wechat.common.dao.digikey;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.DigikeyEventMultiChannel;

@Repository
public interface DigikeyEventMultiChannelDao extends PagingAndSortingRepository<DigikeyEventMultiChannel, Long> {

}
