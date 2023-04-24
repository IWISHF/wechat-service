package com.merkle.wechat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.merkle.wechat.common.dao.ImageAssetsDao;
import com.merkle.wechat.common.entity.ImageAssets;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.Pagination;

@Component
public class ImageAssetsServiceImpl implements ImageAssetsService {
    private @Autowired ImageAssetsDao imageAssetsDao;
    private @Autowired ThirdPartyService ttpServiceImpl;

    @Override
    public String createPicture(MultipartFile pic, Long channelId) throws Exception {
        ImageAssets image = new ImageAssets();
        String fileType = pic.getOriginalFilename();
        fileType = fileType.substring(fileType.lastIndexOf("."));
        image.setFileName(System.currentTimeMillis() + fileType);
        image.setType(pic.getContentType());
        image.setPic(pic.getBytes());
        image.setWechatPublicNoId(channelId);
        image = imageAssetsDao.save(image);
        image.setPath(ttpServiceImpl.getBackendDomain() + "/wechat/" + channelId + "/management/asset/image/get/"
                + image.getId());
        imageAssetsDao.save(image);
        return image.getPath();
    }

    @Override
    public ImageAssets findImageAsset(Long channelId, Long fileId) throws Exception {

        return imageAssetsDao.findByWechatPublicNoIdAndIdAndEnable(channelId, fileId, true);
    }

    @Override
    public Pagination<ImageAssets> search(Long channelId, Pageable pageable, String key) {
        Page<ImageAssets> page = imageAssetsDao.findByFileNameContainingAndWechatPublicNoIdAndEnable(key, channelId, true,
                pageable);
        return new Pagination<>(page);
    }

    @Override
    public List<ImageAssets> getAll(Long channelId) {
        return imageAssetsDao.findByWechatPublicNoIdAndEnableOrderByCreatedDateDesc(channelId, true);
    }

    @Override
    public void deleteImage(Long fileId, Long channelId) {
        ImageAssets image = Optional
                .ofNullable(imageAssetsDao.findByIdAndWechatPublicNoIdAndEnable(fileId, channelId, true))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        imageAssetsDao.delete(image);
    }

}
