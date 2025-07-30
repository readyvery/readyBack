package com.readyvery.readyverydemo.security.jwt.service.sendmanger;

import static com.readyvery.readyverydemo.config.JwtConfig.*;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readyvery.readyverydemo.config.JwtConfig;
import com.readyvery.readyverydemo.security.jwt.service.create.JwtTokenGenerator;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@Getter
@RequiredArgsConstructor
public class JwtTokenizer {

	private final JwtTokenGenerator tokenGenerator;
	private final TokenSendManager tokenSendManager;
	private final ObjectMapper objectMapper;
	private final JwtConfig jwtConfig;

	public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
		tokenSendManager.addTokenCookie(response, jwtConfig.getAccessTokenName(), accessToken, "/",
			jwtConfig.getAccessTokenExpirationPeriod().intValue(), false);
	}

	public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		tokenSendManager.addTokenCookie(response, jwtConfig.getRefreshTokenName(), refreshToken,
			"/api/v1/refresh/token",
			jwtConfig.getRefreshTokenExpirationPeriod().intValue(), true);
	}

	// public void addAccessRefreshTokenResponseBody(String accessToken,
	// 	String refreshToken, Role role) {
	// 	tokenSendManager.addTokenResponseBody(accessToken, refreshToken, role);
	//
	// }

	public Optional<String> verifyAccessToken(String accessToken) {
		// 토큰이 null이거나 빈 문자열인 경우 사전 검사
		if (accessToken == null || accessToken.trim().isEmpty()) {
			log.debug("검증할 AccessToken이 비어있습니다.");
			return Optional.empty();
		}
		
		// JWT 형식 기본 검사
		String[] tokenParts = accessToken.split("\\.");
		if (tokenParts.length != 3) {
			log.debug("AccessToken 검증 실패 - 잘못된 형식입니다. 예상 부분: 3, 실제 부분: {}", tokenParts.length);
			return Optional.empty();
		}
		
		try {
			return Optional.ofNullable(JWT.require(jwtConfig.getAlgorithm())
				.build()
				.verify(accessToken)
				.getClaim(EMAIL_CLAIM)
				.asString());
		} catch (Exception e) {
			log.debug("AccessToken 검증 중 오류 발생: {}", e.getMessage());
			return Optional.empty();
		}
	}
}
