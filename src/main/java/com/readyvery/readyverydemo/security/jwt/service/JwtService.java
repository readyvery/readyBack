package com.readyvery.readyverydemo.security.jwt.service;

import java.util.Optional;

import com.readyvery.readyverydemo.domain.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {

	/**
	 * AccessToken 생성 메소드
	 */
	String createAccessToken(String email);

	/**
	 * RefreshToken 생성
	 * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
	 */
	String createRefreshToken();

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken, Role role);

	/**
	 * 쿠키에서 RefreshToken 추출
	 */
	Optional<String> extractRefreshToken(HttpServletRequest request);

	/**
	 * 쿠키에서 AccessToken 추출
	 */
	Optional<String> extractAccessToken(HttpServletRequest request);

	/**
	 * AccessToken에서 Email 추출
	 * 추출 전에 JWT.require()로 검증기 생성
	 * verify로 AceessToken 검증 후
	 * 유효하다면 getClaim()으로 이메일 추출
	 * 유효하지 않다면 빈 Optional 객체 반환
	 */
	Optional<String> extractEmail(String accessToken);

	/**
	 * RefreshToken DB 저장(업데이트)
	 */

	boolean isTokenValid(String token);

}
