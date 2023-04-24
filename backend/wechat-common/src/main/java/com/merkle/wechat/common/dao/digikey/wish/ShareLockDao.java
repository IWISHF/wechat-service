package com.merkle.wechat.common.dao.digikey.wish;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.merkle.wechat.common.entity.digikey.wish.ShareLock;

@Repository
public interface ShareLockDao extends CrudRepository<ShareLock, Long> {

    @Transactional
    void removeByLock(String lock);

}
