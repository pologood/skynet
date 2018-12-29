package org.shieldproject.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class UserCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserCenterApplication.class, args);
	}

}

