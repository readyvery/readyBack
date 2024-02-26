package com.readyvery.readyverydemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class UserApiConfig {
	@Value("${jwt.refresh.cookie}")
	private String refreshCookie;
	@Value("${service.app.admin.key}")
	private String serviceAppAdminKey;
}
