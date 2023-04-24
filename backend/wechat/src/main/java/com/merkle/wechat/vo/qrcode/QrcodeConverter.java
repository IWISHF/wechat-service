package com.merkle.wechat.vo.qrcode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.merkle.wechat.common.entity.Qrcode;

public class QrcodeConverter {
    public static List<QrcodeVo> convertQrcodeToQrcodeVo(List<Qrcode> content) {
        List<QrcodeVo> vos = new ArrayList<>();
        if (content == null || content.size() == 0) {
            return vos;
        }
        content.forEach((qr) -> {
            QrcodeVo vo = new QrcodeVo();
            BeanUtils.copyProperties(qr, vo, QrcodeVo.class);
            vos.add(vo);
        });
        return vos;
    }
}
