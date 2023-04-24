package com.merkle.wechat.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.ExportLog;

@Repository
public interface ExportLogDao extends CrudRepository<ExportLog, Long> {

    ExportLog findByIdAndWechatPublicNoId(Long id, Long pbNoId);

}
