package com.school.project.framework.services;

import com.school.project.framework.exceptions.SwpException;
import com.school.project.framework.models.App;

public interface SwpAppService {
	App findApp() throws SwpException;
	
	String getAppAccessToken() throws SwpException;
}
