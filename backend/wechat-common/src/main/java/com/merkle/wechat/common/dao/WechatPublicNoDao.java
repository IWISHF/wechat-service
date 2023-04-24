package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.WechatPublicNo;

@Repository
public interface WechatPublicNoDao extends PagingAndSortingRepository<WechatPublicNo, Long> {

    WechatPublicNo findOneByAuthorizerAppid(String authorizer_appid);

    List<WechatPublicNo> findByAccountIdAndStatus(Long accountId, String status);

    List<WechatPublicNo> findByAccountIdAndStatus(Long accountId, String alreadyAuth, Pageable pageable);

    List<WechatPublicNo> findByStatus(String status);

}
