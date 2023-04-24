package com.merkle.wechat.common.dao.digikey;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.TemplateMessageData;

@Repository
public interface TemplateMessageDataDao extends CrudRepository<TemplateMessageData, Long> {

}
