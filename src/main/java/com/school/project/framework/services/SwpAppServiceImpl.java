package com.school.project.framework.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.school.project.framework.exceptions.SwpException;
import com.school.project.framework.models.App;
import com.school.project.framework.models.AppAccessToken;
import com.school.project.framework.repository.SwpAppRepository;

@Component
public class SwpAppServiceImpl implements SwpAppService {
	
	private static final String APP_ID = "wxd3c798b231672956";
	
	private static Map<String, AppAccessToken> map = new HashMap<String, AppAccessToken>();
	
	@Autowired
	private SwpAppRepository appRepository;
	
	@Autowired
	private WxAccessTokenService accessTokenService;

	@Override
	public App findApp() throws SwpException {
		return appRepository.findOne(APP_ID);
	}

	@Override
	@Transactional
	public String getAppAccessToken() throws SwpException {
		AppAccessToken accessToken = null;
		
		//1.Get accessToken from cache
		if (map.containsKey(APP_ID)) {
			accessToken = map.get(APP_ID);
			if (accessToken != null && !accessToken.isExpiresIn()) {
				return accessToken.getAccessToken();
			}
		}
		
		//2.Get accessToken from DataBase
		accessToken = accessTokenService.getAccessTokenFromDB();
		
		//3.Get accessToken from  weixin
		if (accessToken == null) {
			accessToken = accessTokenService.getAccessTokenFromWx();
			accessToken.setCreateTime(new Date());
			accessToken.setAppId(findApp());
			accessToken = accessTokenService.persistenceAccessToken(accessToken);
			map.put(APP_ID, accessToken);
		} else {
			map.put(APP_ID, accessToken);
		}
		
		return accessToken.getAccessToken();
	}
	
	
	
}
