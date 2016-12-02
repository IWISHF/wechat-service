package com.school.project.framework.repository;

import org.springframework.data.repository.CrudRepository;

import com.school.project.framework.models.App;

public interface SwpAppRepository extends CrudRepository<App, String> {
	
}
