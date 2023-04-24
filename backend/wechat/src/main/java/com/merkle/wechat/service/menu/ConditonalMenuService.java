package com.merkle.wechat.service.menu;

import java.util.List;

import com.merkle.wechat.common.entity.menu.ConditionalMenu;
import com.merkle.wechat.vo.menu.ConditionalMenuVo;

public interface ConditonalMenuService {

    List<ConditionalMenuVo> getActiveConditionalMenu(Long channelId) throws Exception;

    void publishConditionalMenu(Long channelId, ConditionalMenu conditionalMenu) throws Exception;

    void delConditionalMenu(Long channelId, Long id) throws Exception;

    ConditionalMenuVo getActiveConditionalMenu(Long channelId, Long id);

}
