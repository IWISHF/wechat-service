package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.Qrcode;

@Repository
public interface QrcodeDao extends PagingAndSortingRepository<Qrcode, Long> {

    Qrcode findByWechatPublicNoIdAndName(Long channelId, String name);

    List<Qrcode> findByToUserNameAndEnable(String toUserName, boolean enbale);

    Qrcode findByWechatPublicNoIdAndId(Long channelId, Long qrcodeId);

    Page<Qrcode> findByNameContainingAndWechatPublicNoId(String key, Long pbNoId, Pageable pageable);

}
