package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.QrcodeDao;
import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.entity.Qrcode;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.autoreply.converter.AutoReplyRuleConverter;
import com.merkle.wechat.vo.qrcode.QrcodeConverter;
import com.merkle.wechat.vo.qrcode.QrcodeDetailVo;
import com.merkle.wechat.vo.qrcode.QrcodeVo;

import weixin.popular.api.QrcodeAPI;
import weixin.popular.bean.qrcode.QrcodeTicket;

@Component
public class QrcodeServiceImpl implements QrcodeService {

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;

    private @Autowired QrcodeDao qrcodeDaoImpl;

    private @Autowired AutoReplyRuleConverter autoReplyRuleConverter;

    private @Autowired TagDao tagDaoImpl;

    private final int MAX_EXPIRE_SECONDS = 2592000;

    @Override
    public void createTempSceneIdQrcode(Long channelId, Long sceneId) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(channelId);
        if (pbNo == null) {
            return;
        }
        QrcodeTicket ticket = QrcodeAPI.qrcodeCreateTemp(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), MAX_EXPIRE_SECONDS, sceneId);
        if (ticket.isSuccess()) {
            Qrcode code = Qrcode.convertFromTicket(ticket);
            code.setAppId(pbNo.getAuthorizerAppid());
            code.setSceneContent(String.valueOf(sceneId));
            code.setType("tempSceneId");
            qrcodeDaoImpl.save(code);
        }
    }

    @Override
    public void createTempSceneStrQrcode(Long channelId, String sceneStr) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(channelId);
        if (pbNo == null) {
            return;
        }
        QrcodeTicket ticket = QrcodeAPI.qrcodeCreateTemp(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), MAX_EXPIRE_SECONDS,
                sceneStr);
        if (ticket.isSuccess()) {
            Qrcode code = Qrcode.convertFromTicket(ticket);
            code.setAppId(pbNo.getAuthorizerAppid());
            code.setSceneContent(sceneStr);
            code.setType("tempSceneStr");
            qrcodeDaoImpl.save(code);
        }
    }

    @Override
    public void createLimitStrQrcode(Long channelId, String sceneStr) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(channelId);
        if (pbNo == null) {
            return;
        }
        QrcodeTicket ticket = QrcodeAPI
                .qrcodeCreateFinal(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), sceneStr);
        if (ticket.isSuccess()) {
            Qrcode code = Qrcode.convertFromTicket(ticket);
            code.setAppId(pbNo.getAuthorizerAppid());
            code.setSceneContent(sceneStr);
            code.setType("limitSceneStr");
            qrcodeDaoImpl.save(code);
        }
    }

    @Override
    public void createLimitQrcode(Long channelId, int sceneId) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneById(channelId);
        if (pbNo == null) {
            return;
        }
        QrcodeTicket ticket = QrcodeAPI
                .qrcodeCreateFinal(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), sceneId);
        if (ticket.isSuccess()) {
            Qrcode code = Qrcode.convertFromTicket(ticket);
            code.setAppId(pbNo.getAuthorizerAppid());
            code.setSceneContent(String.valueOf(sceneId));
            code.setType("limitSceneId");
            qrcodeDaoImpl.save(code);
        }
    }

    @Override
    public void createQrcode(QrcodeVo vo, Long channelId) throws Exception {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        String sceneStr = "scene" + channelId + System.currentTimeMillis();
        QrcodeTicket ticket = QrcodeAPI
                .qrcodeCreateFinal(tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), sceneStr);
        if (ticket.isSuccess()) {
            Qrcode dbCode = qrcodeDaoImpl.findByWechatPublicNoIdAndName(channelId, vo.getName());
            if (dbCode != null) {
                throw new ServiceWarn(ExceptionConstants.NAME_ALREADY_EXIST);
            }
            Qrcode code = Qrcode.convertFromTicket(ticket);
            code = copyFromQrcodeVoForCreate(vo, code);
            code.setWechatPublicNoId(pbNo.getId());
            code.setToUserName(pbNo.getUserName());
            code.setAppId(pbNo.getAuthorizerAppid());
            code.setSceneContent(sceneStr);
            code.setType("limitSceneStr");
            code.setEnable(vo.isEnable());
            qrcodeDaoImpl.save(code);
        } else {
            throw new ServiceWarn(ticket.getErrmsg(), ticket.getErrcode());
        }

    }

    @Override
    public void updateQrcode(QrcodeVo vo, Long channelId, Long qrcodeId) throws Exception {
        Qrcode dbcode = Optional.ofNullable(qrcodeDaoImpl.findByWechatPublicNoIdAndId(channelId, qrcodeId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        dbcode = copyFromVoForUpdate(vo, dbcode);
        dbcode.setId(qrcodeId);
        dbcode.setUpdatedDate(new Date());
        qrcodeDaoImpl.save(dbcode);
    }

    @Override
    public void triggerQrcode(Long qrcodeId, Long channelId, boolean enable) {
        Qrcode dbcode = Optional.ofNullable(qrcodeDaoImpl.findByWechatPublicNoIdAndId(channelId, qrcodeId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        dbcode.setEnable(enable);
        qrcodeDaoImpl.save(dbcode);
    }

    @Override
    public Pagination<QrcodeVo> search(Long pbNoId, String key, Pageable pageable) {
        Page<Qrcode> dbQrcodePage = qrcodeDaoImpl.findByNameContainingAndWechatPublicNoId(key, pbNoId, pageable);
        List<Qrcode> content = dbQrcodePage.getContent();
        List<QrcodeVo> vos = QrcodeConverter.convertQrcodeToQrcodeVo(content);
        Pagination<QrcodeVo> pagination = new Pagination<>();
        BeanUtils.copyProperties(new Pagination<Qrcode>(dbQrcodePage), pagination, "result");
        pagination.setResult(vos);
        return pagination;
    }

    @Override
    public QrcodeDetailVo getQrcodeDetail(Long qrcodeId, Long pbNoId) {
        Qrcode dbcode = Optional.ofNullable(qrcodeDaoImpl.findByWechatPublicNoIdAndId(pbNoId, qrcodeId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        return convertQrcodeToQrcodeDetailVo(dbcode, pbNoId);
    }

    private Qrcode copyFromVoForUpdate(QrcodeVo vo, Qrcode qrcode) {
        qrcode.setAutoTagAlreadySubscribeUser(vo.isAutoTagAlreadySubscribeUser());
        qrcode.setCloseSubscribeAutoReply(vo.isCloseSubscribeAutoReply());
        qrcode.setAutoTagNewSubscribeUser(vo.isAutoTagNewSubscribeUser());
        qrcode.setName(vo.getName());
        qrcode.setDescription(vo.getDescription());
        qrcode.getAlreadySubscribeTags().clear();
        if (vo.getAlreadySubscribeTags() != null) {
            for (Tag tag : vo.getAlreadySubscribeTags()) {
                qrcode.getAlreadySubscribeTags().add(tagDaoImpl.findOne(tag.getId()));
            }
        }

        qrcode.getNewSubscribeTags().clear();
        if (vo.getNewSubscribeTags() != null) {
            for (Tag tag : vo.getNewSubscribeTags()) {
                qrcode.getNewSubscribeTags().add(tagDaoImpl.findOne(tag.getId()));
            }
        }
        qrcode.getAutoReplyrules().clear();
        qrcode.getAutoReplyrules().addAll(vo.getAutoReplyrules());
        qrcode.setEnable(vo.isEnable());
        return qrcode;
    }

    private Qrcode copyFromQrcodeVoForCreate(QrcodeVo vo, Qrcode qrcode) {
        if (vo.getAlreadySubscribeTags() != null) {
            for (Tag tag : vo.getAlreadySubscribeTags()) {
                qrcode.getAlreadySubscribeTags().add(tagDaoImpl.findOne(tag.getId()));
            }
        }
        qrcode.setAutoTagAlreadySubscribeUser(vo.isAutoTagAlreadySubscribeUser());
        qrcode.setAutoTagNewSubscribeUser(vo.isAutoTagNewSubscribeUser());
        qrcode.setCloseSubscribeAutoReply(vo.isCloseSubscribeAutoReply());
        if (vo.getNewSubscribeTags() != null) {
            for (Tag tag : vo.getNewSubscribeTags()) {
                qrcode.getNewSubscribeTags().add(tagDaoImpl.findOne(tag.getId()));
            }
        }
        qrcode.setName(vo.getName());
        qrcode.setDescription(vo.getDescription());
        qrcode.setAutoReplyrules(vo.getAutoReplyrules());
        return qrcode;
    }

    private QrcodeDetailVo convertQrcodeToQrcodeDetailVo(Qrcode qrcode, Long pbNoId) {
        QrcodeDetailVo vo = new QrcodeDetailVo();
        BeanUtils.copyProperties(qrcode, vo, "autoReplyrules");
        vo.setAutoReplyrules(autoReplyRuleConverter.convertAutoReplyRules(qrcode.getAutoReplyrules(), pbNoId));
        return vo;
    }

}
