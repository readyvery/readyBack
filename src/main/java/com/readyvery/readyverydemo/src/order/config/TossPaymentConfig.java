package com.readyvery.readyverydemo.src.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class TossPaymentConfig {
	public static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

	@Value("${payment.toss.client_key}")
	private String tossClientKey;

	@Value("${payment.toss.secret_key}")
	private String tossSecretKey;

	@Value("${payment.toss.success_url}")
	private String tossSuccessUrl;

	@Value("${payment.toss.fail_url}")
	private String tossFailUrl;
}
