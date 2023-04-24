package com.merkle.wechat.common.dao.digikey.wish;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.wish.WishItem;

@Repository
public interface WishItemDao extends CrudRepository<WishItem, Long> {
    List<WishItem> findByCampaignId(Long campaignId);
}
