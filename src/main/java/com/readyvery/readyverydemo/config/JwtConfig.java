package com.readyvery.readyverydemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;

import lombok.Getter;

@Configuration
@Getter
public class JwtConfig {

	private final String secretKey;
	private final Long accessTokenExpirationPeriod;
	private final Long refreshTokenExpirationPeriod;
	private final String accessTokenName;
	private final String refreshTokenName;
	private final String userFrontendUrl;
	private final String guestFrontendUrl;
	private final Algorithm algorithm;

	public static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	public static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	public static final String EMAIL_CLAIM = "email";
	public static final String USER_NUMBER = "userNumber";
	public static final String BEARER = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";

	public JwtConfig(
		@Value("${jwt.secretKey}") String secretKey,
		@Value("${jwt.access.expiration}") Long accessTokenExpirationPeriod,
		@Value("${jwt.refresh.expiration}") Long refreshTokenExpirationPeriod,
		@Value("${jwt.access.cookie}") String accessTokenName,
		@Value("${jwt.refresh.cookie}") String refreshTokenName,
		@Value("${jwt.redirect-uri-user}") String userFrontendUrl,
		@Value("${jwt.redirect-uri-guest}") String guestFrontendUrl

	) {
		this.secretKey = secretKey;
		this.accessTokenExpirationPeriod = accessTokenExpirationPeriod;
		this.refreshTokenExpirationPeriod = refreshTokenExpirationPeriod;
		this.accessTokenName = accessTokenName;
		this.refreshTokenName = refreshTokenName;
		this.userFrontendUrl = userFrontendUrl;
		this.guestFrontendUrl = guestFrontendUrl;
		this.algorithm = initializeAlgorithm(secretKey);
	}

	private Algorithm initializeAlgorithm(String secretKey) {
		if (secretKey == null || secretKey.trim().isEmpty()) {
			throw new IllegalArgumentException("Secret key must not be null or empty");
		}
		return Algorithm.HMAC512(secretKey);
	}
}
