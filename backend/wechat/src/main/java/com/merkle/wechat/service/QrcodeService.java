package com.merkle.wechat.service;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.qrcode.QrcodeDetailVo;
import com.merkle.wechat.vo.qrcode.QrcodeVo;

public interface QrcodeService {

    void createTempSceneIdQrcode(Long channelId, Long sceneId);

    void createTempSceneStrQrcode(Long channelId, String sceneStr);

    void createLimitStrQrcode(Long channelId, String sceneStr);

    void createLimitQrcode(Long channelId, int sceneId);

    void createQrcode(QrcodeVo vo, Long channelId) throws Exception;

    void updateQrcode(QrcodeVo vo, Long channelId, Long qrcodeId) throws Exception;

    void triggerQrcode(Long qrcodeId, Long channelId, boolean enable);

    Pagination<QrcodeVo> search(Long channelId, String key, Pageable pageable);

    QrcodeDetailVo getQrcodeDetail(Long qrcodeId, Long channelId);

}
