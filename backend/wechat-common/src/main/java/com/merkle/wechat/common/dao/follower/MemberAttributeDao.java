package com.merkle.wechat.common.dao.follower;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.follower.MemberAttribute;

@Repository
public interface MemberAttributeDao extends PagingAndSortingRepository<MemberAttribute, Long> {

    MemberAttribute findOneByTypeAndOpenid(String type, String openid);

    int countByTypeAndOpenid(String type, String openid);

    List<MemberAttribute> findByOpenid(String openid);

}
