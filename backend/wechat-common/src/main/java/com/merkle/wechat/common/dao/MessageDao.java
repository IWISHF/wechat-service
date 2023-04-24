package com.merkle.wechat.common.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.EventMessage;

@Repository
public interface MessageDao extends PagingAndSortingRepository<EventMessage, Long> {

    List<EventMessage> findByFromUserNameAndCreateTime(String fromUserName, Long time);

    @Query("select distinct fromUserName from eventmessage where content like '%电子展%' or content like '%慕尼黑%' and appId=:appIdStr")
    Set<String> getDistinctEventMessageForDigikeyMunihei(@Param("appIdStr") String appIdStr);

}
