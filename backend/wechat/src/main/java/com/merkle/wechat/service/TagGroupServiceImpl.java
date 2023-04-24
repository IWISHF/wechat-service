package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.dao.TagGroupDao;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.Pagination;

@Component
public class TagGroupServiceImpl implements TagGroupService {
    private @Autowired TagGroupDao tagGroupDaoImpl;
    private @Autowired TagDao tagDaoImpl;

    @Override
    public List<TagGroup> getAllGroup(Long wechatPublicNoId) throws Exception {
        return tagGroupDaoImpl.findAllByWechatPublicNoId(wechatPublicNoId);
    }

    @Override
    public Pagination<TagGroup> getAllGroup(Long wechatPublicNoId, Pageable pageable) {
        Page<TagGroup> page = tagGroupDaoImpl.findAllByWechatPublicNoId(wechatPublicNoId, pageable);
        return new Pagination<TagGroup>(page);
    }

    @Override
    public void createTagGroup(TagGroup group, WechatPublicNo pbNo) throws Exception {
        boolean exist = tagGroupDaoImpl.existsByNameAndWechatPublicNoId(group.getName(), pbNo.getId());
        if (exist) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }
        group.setDefaultGroup(false);
        group.setWechatPublicNoId(pbNo.getId());
        tagGroupDaoImpl.save(group);
    }

    @Override
    public void deleteTagGroup(Long groupId, WechatPublicNo pbNo) {
        TagGroup group = Optional.ofNullable(tagGroupDaoImpl.findByIdAndWechatPublicNoId(groupId, pbNo.getId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (group.isDefaultGroup()) {
            throw new ServiceWarn(ExceptionConstants.DEFAULT_GROUP_CANT_DELETE);
        }

        List<Tag> tags = tagDaoImpl.findByGroup(group);
        if (tags != null && !tags.isEmpty()) {
            TagGroup defaultGroup = getDefaultGroup(pbNo);
            tags.forEach((tag) -> {
                tag.setGroup(defaultGroup);
                tag.setUpdatedDate(new Date());
            });
            tagDaoImpl.save(tags);
        }
        tagGroupDaoImpl.delete(group);
    }

    private TagGroup getDefaultGroup(WechatPublicNo pbNo) {
        return Optional.ofNullable(tagGroupDaoImpl.findByNameAndWechatPublicNoId(TagGroup.DEFAULT_GROUP, pbNo.getId()))
                .orElseGet(() -> {
                    return createDefaultGroup(pbNo);
                });
    }

    private TagGroup createDefaultGroup(WechatPublicNo pbNo) {
        TagGroup group = new TagGroup();
        group.setDefaultGroup(true);
        group.setName(TagGroup.DEFAULT_GROUP);
        group.setWechatPublicNoId(pbNo.getId());
        return tagGroupDaoImpl.save(group);
    }

    @Override
    public void updateTagGroup(TagGroup group, Long groupId, WechatPublicNo pbNo) {
        TagGroup dbGroup = Optional.ofNullable(tagGroupDaoImpl.findByIdAndWechatPublicNoId(groupId, pbNo.getId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        boolean exist = tagGroupDaoImpl.existsByNameAndWechatPublicNoIdAndIdNot(group.getName(), pbNo.getId(),
                dbGroup.getId());
        if (exist) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }
        if (dbGroup.isDefaultGroup()) {
            throw new ServiceWarn(ExceptionConstants.DEFAULT_GROUP_CANT_EDIT);
        }
        dbGroup.setName(group.getName());
        tagGroupDaoImpl.save(dbGroup);
    }

}
