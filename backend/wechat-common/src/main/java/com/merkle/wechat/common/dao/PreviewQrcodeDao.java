package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.PreviewQrcode;

@Repository
public interface PreviewQrcodeDao extends CrudRepository<PreviewQrcode, Long> {

    List<PreviewQrcode> findByToUserName(String toUserName);

}
