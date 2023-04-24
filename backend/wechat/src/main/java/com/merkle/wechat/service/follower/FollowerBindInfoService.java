package com.merkle.wechat.service.follower;

import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.vo.follower.FollowerApiInfoVo;

public interface FollowerBindInfoService {

    FollowerBindInfo findOneByOpenid(String openid);

    void create(FollowerBindInfo bindInfo) throws Exception;

    void update(Long id, FollowerBindInfo bindInfo) throws Exception;

    FollowerApiInfoVo getFollowerInfo(String id) throws Exception;

    void fixSyncFollowerInfoFailed() throws Exception;

    FollowerApiInfoVo findFollowerWithIncludes(String openid, String includes) throws Exception;
}
