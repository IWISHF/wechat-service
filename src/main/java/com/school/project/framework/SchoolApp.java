package com.school.project.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class SchoolApp {
	//application entry
    public static void main(String[] args) {
        SpringApplication.run(SchoolApp.class, args);
    }
}
