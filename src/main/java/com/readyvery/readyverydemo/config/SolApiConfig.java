package com.readyvery.readyverydemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Configuration
public class SolApiConfig {
	public static final String SOLAPI_URL = "https://api.solapi.com";

	@Value("${solapi.api_key}")
	private String apiKey;

	@Value("${solapi.secret_key}")
	private String apiSecret;

	@Value("${solapi.templete.cancel}")
	private String templeteCancel;

	@Value("${solapi.templete.pickup}")
	private String templetePickup;

	@Value("${solapi.templete.order}")
	private String templeteOrder;

	@Value("${solapi.kakao.pfid}")
	private String kakaoPfid;

	@Value("${solapi.phone_number}")
	private String phoneNumber;

	@Bean
	public DefaultMessageService defaultMessageService() {
		return NurigoApp.INSTANCE.initialize(apiKey, apiSecret, SOLAPI_URL);
	}
}
