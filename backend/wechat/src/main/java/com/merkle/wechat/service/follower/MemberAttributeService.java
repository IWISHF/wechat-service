package com.merkle.wechat.service.follower;

import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.follower.MemberAttribute;

public interface MemberAttributeService {

    MemberAttribute genereateMemberAttribute(FollowerBindInfo member, String type, int limit) throws Exception;
}
