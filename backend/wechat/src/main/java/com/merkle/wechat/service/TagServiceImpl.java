package com.merkle.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.QrcodeDao;
import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.dao.TagGroupConditionDao;
import com.merkle.wechat.common.dao.TagGroupDao;
import com.merkle.wechat.common.dao.batch.BatchTaskDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.dao.menu.ConditionalMenuDao;
import com.merkle.wechat.common.entity.Qrcode;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.common.entity.TagGroupCondition;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.batch.BatchTask;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.menu.ConditionalMenu;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.menu.ConditonalMenuService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.batch.BatchTaskConverter;
import com.merkle.wechat.vo.batch.BatchTaskVo;
import com.merkle.wechat.vo.menu.converter.MenuConverter;
import com.merkle.wechat.vo.qrcode.QrcodeConverter;
import com.merkle.wechat.vo.qrcode.QrcodeVo;
import com.merkle.wechat.vo.tag.TagVo;
import com.merkle.wechat.weixin.WeixinUserAdapter;

import weixin.popular.api.UserAPI;
import weixin.popular.bean.BaseResult;

@Component
public class TagServiceImpl implements TagService {
    protected Logger logger = LoggerFactory.getLogger("TagServiceImpl");
    private @Autowired TagDao tagDaoImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired TagGroupDao tagGroupDaoImpl;
    private @Autowired WeixinUserAdapter weixinUserAdapterImpl;
    private @Autowired BatchTaskConverter batchTaskConverter;
    private @Autowired ConditionalMenuDao conditionalMenuDaoImpl;
    private @Autowired MenuConverter menuConverter;

    @Override
    public void tagFollower(WechatPublicNo pbNo, String openid, Integer tagId) {
        UserAPI.tagsMembersBatchtagging(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()),
                tagId, new String[] { openid });
    }

    @Override
    public void updateTag(WechatPublicNo pbNo, Long tagId, Tag tag) {
        Tag dbTag = Optional.ofNullable(tagDaoImpl.findByIdAndWechatPublicNoId(tagId, pbNo.getId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (tag.getGroup() != null) {
            dbTag.setGroup(tag.getGroup());
        }
        if (!dbTag.isFromWechat() && tag.isFromWechat()) {
            dbTag = weixinUserAdapterImpl.syncOrCreateWeixinTag(dbTag, pbNo);
        }
        dbTag.setUpdatedDate(new Date());
        tagDaoImpl.save(dbTag);
    }

    @Override
    public void createTag(WechatPublicNo pbNo, Tag tag) {
        if (tagDaoImpl.findByNameAndWechatPublicNoId(tag.getName(), pbNo.getId()) != null) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }

        if (tag.getGroup() == null) {
            tag.setGroup(getDefaultGroup(pbNo));
        }

        if (tag.isFromWechat()) {
            tag = weixinUserAdapterImpl.syncOrCreateWeixinTag(tag, pbNo);
        }
        tag.setWechatPublicNoId(pbNo.getId());
        tagDaoImpl.save(tag);
    }

    @Override
    public void syncTagFromWechat(WechatPublicNo pbNo) {
        weixinUserAdapterImpl.syncTagFromWechat(pbNo, getDefaultGroup(pbNo));
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
    public Tag getTagByName(String name, WechatPublicNo pbNo) {
        return tagDaoImpl.findByNameAndWechatPublicNoId(name, pbNo.getId());
    }

    @Override
    public Pagination<TagVo> searchWeixinTag(String name, Long groupId, WechatPublicNo pbNo, Pageable pageable) {
        if (groupId < 0) {
            return convertToTagVoPagination(new Pagination<Tag>(tagDaoImpl
                    .findByNameContainingAndWechatPublicNoIdAndFromWechat(name, pbNo.getId(), true, pageable)));
        }
        return convertToTagVoPagination(
                new Pagination<Tag>(tagDaoImpl.findByNameContainingAndGroup_IdAndWechatPublicNoIdAndFromWechat(name,
                        groupId, pbNo.getId(), true, pageable)));
    }

    @Override
    public Tag getTagByWeixinTagId(Integer tagId, Long pbNoId) {
        return tagDaoImpl.findByTagIdAndWechatPublicNoId(Long.valueOf(tagId), pbNoId);
    }

    @Override
    public Pagination<TagVo> searchTag(String name, Long groupId, WechatPublicNo pbNo, Pageable pageable) {
        if (groupId < 0) {
            return convertToTagVoPagination(new Pagination<Tag>(
                    tagDaoImpl.findByNameContainingAndWechatPublicNoId(name, pbNo.getId(), pageable)));
        }
        return convertToTagVoPagination(new Pagination<Tag>(
                tagDaoImpl.findByNameContainingAndGroup_IdAndWechatPublicNoId(name, groupId, pbNo.getId(), pageable)));
    }

    @Override
    public List<Tag> getTagByIds(long[] tagIds, WechatPublicNo pbNo) {
        return tagDaoImpl.findByIdInAndWechatPublicNoId(tagIds, pbNo.getId());
    }

    @Override
    public void save(Tag tag) {
        tagDaoImpl.save(tag);
    }

    Pagination<TagVo> convertToTagVoPagination(Pagination<Tag> tagPage) {
        Pagination<TagVo> page = new Pagination<>();
        BeanUtils.copyProperties(tagPage, page, "result");
        List<Tag> tags = tagPage.getResult();
        page.setResult(convertTagsToTagVos(tags));
        return page;
    }

    private List<TagVo> convertTagsToTagVos(List<Tag> tags) {
        List<TagVo> vos = new ArrayList<>();
        if (tags == null) {
            return vos;
        }
        tags.forEach((t) -> {
            vos.add(convertTagToTagVo(t));
        });
        return vos;
    }

    private TagVo convertTagToTagVo(Tag t) {
        TagVo vo = new TagVo();
        BeanUtils.copyProperties(t, vo);
        vo.setQrcodes(convertQrcodeToQrcodeVo(t.getAlreadySubscribeTagQrcodes(), t.getNewSubscribeTagOrcodes()));
        // TODO:
        // vo.setBatchJobs(convertBatchTasksToBatchTaskVos(t.getBatchJobs()));
        List<ConditionalMenu> menus = conditionalMenuDaoImpl.findByMatchrule_tag_id(t.getId());
        menus = menus.stream().filter((m) -> m.isPublished()).collect(Collectors.toList());
        vo.setMenus(menuConverter.convertConditionalMenusToVos(menus, t.getWechatPublicNoId()));
        return vo;
    }

    @SuppressWarnings("unused")
    private List<BatchTaskVo> convertBatchTasksToBatchTaskVos(Set<BatchTask> batchJobs) {
        if (batchJobs != null) {
            return null;
        }
        @SuppressWarnings("null")
        List<BatchTaskVo> vos = batchTaskConverter.convertTasksToVos(
                batchJobs.stream().filter((t) -> !t.isAlreadyExecuted()).collect(Collectors.toList()));
        return vos;
    }

    private List<QrcodeVo> convertQrcodeToQrcodeVo(Set<Qrcode> alreadySubscribeTagQrcodes,
            Set<Qrcode> newSubscribeTagOrcodes) {
        List<Qrcode> codes = new ArrayList<>();
        if (alreadySubscribeTagQrcodes != null) {
            codes.addAll(alreadySubscribeTagQrcodes);
        }
        if (newSubscribeTagOrcodes != null) {
            codes.addAll(newSubscribeTagOrcodes);
        }
        codes = codes.stream().filter((q) -> {
            return q.isEnable();
        }).collect(Collectors.toList());

        return QrcodeConverter.convertQrcodeToQrcodeVo(codes);
    }

    @Override
    @Transactional
    public String deleteTag(WechatPublicNo pbNo, Long id) throws Exception {
        Tag tag = Optional.ofNullable(tagDaoImpl.findByIdAndWechatPublicNoId(id, pbNo.getId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        futures.add(AsyncUtil.asyncRunWithReturn(() -> removeTagFromQrcodes(tag)));
        futures.add(
                AsyncUtil.asyncRunWithReturn(() -> removeTagFromTagGroupConditions(tag.getTagGroupConditions(), tag)));
        futures.add(AsyncUtil.asyncRunWithReturn(() -> {
            try {
                return removeConditionalMenus(tag);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));
        futures.add(AsyncUtil.asyncRunWithReturn(() -> {
            try {
                return removeTagFromFollowers(tag, pbNo);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }));

        boolean success = futures.stream().map(CompletableFuture::join).allMatch((finished) -> finished);

        if (success) {
            if (tag.isFromWechat()) {
                removeWeixinTag(pbNo, tag);
            } else {
                tagDaoImpl.delete(tag);
            }
            return "ok";
        } else {
            throw new ServiceWarn(ExceptionConstants.UN_KNOWN_ERROR);
        }

    }

    private @Autowired TagGroupConditionDao tagGroupCondtionDaoImpl;

    private boolean removeTagFromTagGroupConditions(Set<TagGroupCondition> tagGroupConditions, Tag tag) {
        logger.info("===== start remove batch task group conditions=====");
        if (tagGroupConditions == null || tagGroupConditions.size() == 0) {
            logger.info("===== end remove batch task group conditions=====");
            return true;
        }
        List<TagGroupCondition> editConditions = new ArrayList<>();
        tagGroupConditions.forEach((g) -> {
            Set<Tag> tags = g.getTags();
            if (tags != null && tags.contains(tag)) {
                g.getTags().remove(tag);
                editConditions.add(g);
            }
        });
        tagGroupCondtionDaoImpl.save(tagGroupConditions);
        logger.info("===== end remove batch task groupConditions =====");
        return true;
    }

    private @Autowired FollowerDao followerDaoImpl;

    private boolean removeTagFromFollowers(Tag tag, WechatPublicNo pbNo) throws Exception {
        logger.info("===== start remove follower tag =====");
        removeFollowersTag(tag, pbNo);
        logger.info("===== end remove follower tag =====");
        return true;
    }

    public void removeFollowersTag(Tag tag, WechatPublicNo pbNo) throws Exception {
        followerDaoImpl.deleteFollowersTag(tag.getId());

        if (tag.isFromWechat()) {
            if (tag.getCount() >= 100000) {
                Set<Follower> followers = tag.getTagFollowers();
                List<Follower> editFollowers = new ArrayList<>();
                editFollowers.addAll(followers);
                // 微信标签删除限制
                int batchApiRemoveNumber = followers.size() - 99998;
                List<String> needToRemoveOpenids = editFollowers.subList(0, batchApiRemoveNumber).stream()
                        .map((f) -> f.getOpenid()).collect(Collectors.toList());
                int start = 0;
                int end = needToRemoveOpenids.size() - 1;
                int max = needToRemoveOpenids.size() - 1;

                while (start <= max) {
                    List<String> currentRemoveOpenids = needToRemoveOpenids.subList(start, end + 1);
                    String[] openids = currentRemoveOpenids.toArray(new String[currentRemoveOpenids.size()]);
                    BaseResult weixinResult = UserAPI.tagsMembersBatchuntagging(
                            tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()),
                            tag.getTagId().intValue(), openids);
                    if (!weixinResult.isSuccess()) {
                        throw new ServiceWarn(weixinResult.getErrmsg(), weixinResult.getErrcode());
                    }
                    start = end + 1;
                    end += 49;
                    if (end > max) {
                        end = max;
                    }
                }
            }
        }

    }

    private void removeWeixinTag(WechatPublicNo pbNo, Tag tag) {
        BaseResult tagsDeleteResult = UserAPI.tagsDelete(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), tag.getTagId().intValue());
        if (!tagsDeleteResult.isSuccess()) {
            throw new ServiceWarn(tagsDeleteResult.getErrmsg(), tagsDeleteResult.getErrcode());
        }
        tagDaoImpl.delete(tag);
    }

    private @Autowired ConditonalMenuService conditionalMenuServiceImpl;

    private boolean removeConditionalMenus(Tag tag) throws Exception {
        logger.info("===== start remove conditional menu =====");
        List<ConditionalMenu> menus = conditionalMenuDaoImpl.findByMatchrule_tag_id(tag.getId());
        removeConditionalMenus(menus, tag);
        logger.info("===== end remove conditional menu =====");
        return true;
    }

    private void removeConditionalMenus(List<ConditionalMenu> menus, Tag tag) throws Exception {
        if (menus == null || menus.size() == 0) {
            return;
        }
        List<ConditionalMenu> editMenus = new ArrayList<>();
        for (ConditionalMenu m : menus) {
            if (m.isPublished()) {
                conditionalMenuServiceImpl.delConditionalMenu(tag.getWechatPublicNoId(), m.getId());
            } else {
                m.getMatchrule().setTag(null);
                editMenus.add(m);
            }
        }

        conditionalMenuDaoImpl.save(editMenus);
    }

    private @Autowired BatchTaskDao batchTaskDaoImpl;

    @SuppressWarnings("unused")
    private boolean removeTagFromBatchTasks(Set<BatchTask> tasks, Tag tag) {
        logger.info("===== start remove batch task =====");
        if (tasks == null || tasks.size() == 0) {
            logger.info("===== end remove batch task =====");
            return true;
        }
        List<BatchTask> editTasks = new ArrayList<>();
        tasks.forEach((t) -> {
            Set<TagGroupCondition> groupConditions = t.getGroupConditions();
            if (groupConditions != null) {
                groupConditions.forEach((g) -> {
                    Set<Tag> tags = g.getTags();
                    if (tags != null && tags.contains(tag)) {
                        g.getTags().remove(tag);
                        editTasks.add(t);
                    }
                });
            }

        });
        batchTaskDaoImpl.save(editTasks);
        logger.info("===== end remove batch task =====");
        return true;
    }

    private @Autowired QrcodeDao qrcodeDaoImpl;

    private boolean removeTagFromQrcodes(Tag tag) {
        logger.info("===== start remove qrcodes =====");
        removeTagFromQrcodes(tag.getAlreadySubscribeTagQrcodes(), tag);
        removeTagFromQrcodes(tag.getNewSubscribeTagOrcodes(), tag);
        logger.info("===== end remove qrcodes =====");
        return true;
    }

    private void removeTagFromQrcodes(Set<Qrcode> qrcodes, Tag t) {
        if (qrcodes == null) {
            return;
        }
        List<Qrcode> editQrcodes = new ArrayList<>();
        qrcodes.forEach((q) -> {
            boolean edit = false;
            if (q.getAlreadySubscribeTags() != null && q.getAlreadySubscribeTags().contains(t)) {
                q.getAlreadySubscribeTags().remove(t);
                edit = true;
            }

            if (q.getNewSubscribeTags() != null && q.getNewSubscribeTags().contains(t)) {
                q.getNewSubscribeTags().remove(t);
                edit = true;
            }

            if (edit) {
                q.setEnable(false);
                editQrcodes.add(q);
            }
        });
        qrcodeDaoImpl.save(editQrcodes);
    }

}
