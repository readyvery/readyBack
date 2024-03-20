package com.readyvery.readyverydemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Getter
public class OauthConfig {

	@Value("${app.apple.url}")
	private String appleUrl;

	@Value("${app.apple.private-key}")
	private String privateKeyString;
	@Value("${app.apple.client-id}")
	private String appleClientId;

	@Value("${app.apple.team-id}")
	private String appleTeamId;

	@Value("${app.apple.key-id}")
	private String appleKeyId;
	public static final String KAKAO_NAME = "kakao";
	public static final String APPLE_NAME = "apple";
}
