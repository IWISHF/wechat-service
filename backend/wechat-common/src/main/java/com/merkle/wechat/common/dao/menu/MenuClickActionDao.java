package com.merkle.wechat.common.dao.menu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.menu.MenuClickAction;

@Repository
public interface MenuClickActionDao extends CrudRepository<MenuClickAction, Long> {

    List<MenuClickAction> findByToUserNameAndEnable(String toUserName, boolean active);

}
