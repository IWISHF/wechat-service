package com.merkle.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@EnableAdminServer
public class AdminMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminMonitorApplication.class, args);
	}

}
