package com.merkle.wechat.common.dao.digikey;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.DigikeyLikeOrUnlikeRecord;

@Repository
public interface DigikeyLikeOrUnlikeRecordDao extends PagingAndSortingRepository<DigikeyLikeOrUnlikeRecord, Long> {

    DigikeyLikeOrUnlikeRecord findOneByIdentityIdAndPageAlias(String identityId, String pageAlias);

    long countByPageAliasAndVote(String pageAlias, String like);

}
