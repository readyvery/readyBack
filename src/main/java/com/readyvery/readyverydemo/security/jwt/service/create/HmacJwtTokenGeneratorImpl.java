package com.readyvery.readyverydemo.security.jwt.service.create;

import static com.readyvery.readyverydemo.config.JwtConfig.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.readyvery.readyverydemo.config.JwtConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class HmacJwtTokenGeneratorImpl implements JwtTokenGenerator {

	private final JwtConfig jwtConfig;

	@Override
	public String generateAccessToken(String email, Long id) {
		// 토큰 생성 로직
		Instant now = Instant.now();
		Instant expirationTime = now.plus(jwtConfig.getAccessTokenExpirationPeriod(), ChronoUnit.SECONDS);
		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(Date.from(expirationTime))
			.withClaim(EMAIL_CLAIM, email)
			.withClaim(USER_NUMBER, id)
			.sign(jwtConfig.getAlgorithm());
	}

	@Override
	public String generateRefreshToken() {
		// 토큰 생성 로직
		Instant now = Instant.now();
		Instant expirationTime = now.plus(jwtConfig.getRefreshTokenExpirationPeriod(), ChronoUnit.SECONDS);
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(Date.from(expirationTime))
			.sign(jwtConfig.getAlgorithm());
	}
}
