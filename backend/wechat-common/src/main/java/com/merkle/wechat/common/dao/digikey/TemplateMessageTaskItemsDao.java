package com.merkle.wechat.common.dao.digikey;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.TemplateMessageTaskItems;

@Repository
public interface TemplateMessageTaskItemsDao extends CrudRepository<TemplateMessageTaskItems, Long> {

    List<TemplateMessageTaskItems> findAllByTaskId(Long taskId);

}
