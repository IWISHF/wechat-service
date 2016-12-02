package com.school.project.framework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.school.project.framework.exceptions.SwpException;
import com.school.project.framework.models.AppAccessToken;
import com.school.project.framework.repository.WxAccessTokenRepository;
import com.school.project.framework.utils.JsonUtil;
import com.school.project.framework.wxapi.WxApiSolution;

@Component
public class WxAccessTokenServiceImpl implements WxAccessTokenService {
	@Autowired
	private WxApiSolution solution;
	
	@Autowired
	private WxAccessTokenRepository accessTokenRepository;
	
	@Autowired
	private SwpAppService appService;
	
	@Override
	public AppAccessToken getAccessTokenFromWx() throws SwpException {
		AppAccessToken token = null;
		token = JsonUtil.jsonStrToObjet(solution.getAccessToken(), AppAccessToken.class);
		return token;
	}
	
	@Override
	public AppAccessToken persistenceAccessToken(AppAccessToken accessToken) throws SwpException {
		AppAccessToken oldToken = accessTokenRepository.findByAppId(appService.findApp());
		if (oldToken != null) {
			//do update
			//TODO .... it's not a good solution
			accessToken.setCreateTime(oldToken.getCreateTime());
			accessTokenRepository.save(accessToken);
			accessTokenRepository.delete(oldToken.getAccessToken());
		} else {
			accessToken = accessTokenRepository.save(accessToken);
		}
		return accessToken;
	}

	@Override
	public AppAccessToken getAccessTokenFromDB() throws SwpException {
		AppAccessToken token = null;
		token = accessTokenRepository.findByAppId(appService.findApp());
		if (token != null && !token.isExpiresIn()) {
			return token;
		}
		return null;
	}

}
