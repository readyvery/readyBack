package com.readyvery.readyverydemo.security.jwt.service;

import static com.readyvery.readyverydemo.config.JwtConfig.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.readyvery.readyverydemo.config.JwtConfig;
import com.readyvery.readyverydemo.domain.Role;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.security.jwt.service.create.JwtTokenGenerator;
import com.readyvery.readyverydemo.security.jwt.service.extract.ExtractToken;
import com.readyvery.readyverydemo.security.jwt.service.sendmanger.JwtTokenizer;
import com.readyvery.readyverydemo.src.user.UserServiceFacade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService {

	private final UserServiceFacade userServiceFacade;
	private final JwtTokenizer jwtTokenizer;
	private final JwtTokenGenerator hmacJwtTokenGeneratorImpl;
	private final JwtConfig jwtConfig;
	private final ExtractToken extractToken;

	/**
	 * AccessToken 생성 메소드
	 */
	@Override
	public String createAccessToken(String email) {
		UserInfo userInfo = userServiceFacade.getUserInfoByEmail(email);
		return hmacJwtTokenGeneratorImpl.generateAccessToken(email, userInfo.getId());
	}

	/**
	 * RefreshToken 생성
	 * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
	 */
	@Override
	public String createRefreshToken() {

		return hmacJwtTokenGeneratorImpl.generateRefreshToken();
	}

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	@Override
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken,
		Role role) {

		jwtTokenizer.addAccessTokenCookie(response, accessToken);

		jwtTokenizer.addRefreshTokenCookie(response, refreshToken);

	}

	/**
	 * RefreshToken 추출
	 */
	@Override
	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return extractToken.extractTokenCookie(request, jwtConfig.getRefreshTokenName());
	}

	/**
	 * AccessToken 추출
	 */
	@Override
	public Optional<String> extractAccessToken(HttpServletRequest request) {
		// "Authorization" 헤더를 확인합니다.
		//String authorizationHeader = request.getHeader(AUTHORIZATION);
		// "Authorization" 헤더가 존재하면, 헤더에서 토큰을 추출합니다.
		//if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
		return extractToken.extractTokenHeader(request, AUTHORIZATION);
		//}

		// "Authorization" 헤더가 존재하지 않으면, 쿠키에서 토큰을 추출합니다.
		//return extractToken.extractTokenCookie(request, jwtConfig.getAccessTokenName());

	}

	/**
	 * AccessToken에서 Email 추출
	 * 추출 전에 JWT.require()로 검증기 생성
	 * verify로 AceessToken 검증 후
	 * 유효하다면 getClaim()으로 이메일 추출
	 * 유효하지 않다면 빈 Optional 객체 반환
	 */
	@Override
	public Optional<String> extractEmail(String accessToken) {
		// 토큰이 null이거나 빈 문자열인 경우 사전 검사
		if (accessToken == null || accessToken.trim().isEmpty()) {
			log.debug("AccessToken이 비어있습니다.");
			return Optional.empty();
		}
		
		// JWT 형식 기본 검사
		String[] tokenParts = accessToken.split("\\.");
		if (tokenParts.length != 3) {
			log.debug("잘못된 AccessToken 형식입니다. 예상 부분: 3, 실제 부분: {}", tokenParts.length);
			return Optional.empty();
		}
		
		try {
			// 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
			return jwtTokenizer.verifyAccessToken(accessToken);
		} catch (Exception e) {
			log.debug("AccessToken에서 이메일 추출 실패: {}", e.getMessage());
			return Optional.empty();
		}
	}

	@Override
	public boolean isTokenValid(String token) {
		// 토큰이 null이거나 빈 문자열인 경우 사전 검사
		if (token == null || token.trim().isEmpty()) {
			log.debug("토큰이 비어있습니다.");
			return false;
		}
		
		// JWT 형식 기본 검사 (3개 부분으로 구성되어야 함)
		String[] tokenParts = token.split("\\.");
		if (tokenParts.length != 3) {
			log.debug("잘못된 JWT 토큰 형식입니다. 예상 부분: 3, 실제 부분: {}", tokenParts.length);
			return false;
		}
		
		try {
			JWT.require(jwtConfig.getAlgorithm()).build().verify(token);
			return true;
		} catch (Exception e) {
			log.debug("JWT 토큰 검증 실패: {}", e.getMessage());
			return false;
		}
	}

}
