package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.AnnualVote;

@Repository
public interface AnnualVoteDao extends CrudRepository<AnnualVote, Long>{

    AnnualVote findOneByOpenid(String openid);

}
