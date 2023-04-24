package com.merkle.wechat.common.dao.digikey.wish;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.wish.WishItemLightLog;

@Repository
public interface WishItemLightLogDao extends PagingAndSortingRepository<WishItemLightLog, Long> {

    Set<WishItemLightLog> findByOpenid(String openid);

    @Query(nativeQuery = true, value = "select openid from digikey_wish_item_light_log group by openid having count(1)>=24")
    List<String> findWishPerfectOpenids();

    int countByWishItemIdAndRecordEventStatusAndOpenid(Long wishItemId, boolean status, String openid);

}
