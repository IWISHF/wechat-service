package com.merkle.wechat.service.follower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.follower.MemberAttributeDao;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.entity.follower.MemberAttribute;
import com.merkle.wechat.common.exception.ServiceError;
import com.merkle.wechat.constant.ExceptionConstants;

@Component
public class MemberAttributeServiceImpl implements MemberAttributeService {
    private @Autowired MemberAttributeDao memberAttributeDao;

    @Override
    public MemberAttribute genereateMemberAttribute(FollowerBindInfo member, String type, int limit) throws Exception {
        int cnt = memberAttributeDao.countByTypeAndOpenid(type, member.getOpenid());
        if (cnt >= limit) {
            throw new ServiceError("Member attributes already exist!", ExceptionConstants.ALREADY_EXIST);
        }

        MemberAttribute attribute = new MemberAttribute();
        attribute.setMemberId(member.getId());
        attribute.setOpenid(member.getOpenid());
        attribute.setType(type);
        attribute.setValue(generateOrderCode(member));

        memberAttributeDao.save(attribute);

        return attribute;
    }

    private String generateOrderCode(FollowerBindInfo member) {
        String preText = "DKCR42021-WR";
        return preText + member.getId();
    }

}
