package com.readyvery.readyverydemo.security.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.config.JwtConfig;
import com.readyvery.readyverydemo.domain.Role;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.security.jwt.service.JwtService;
import com.readyvery.readyverydemo.security.oauth2.CustomOAuth2User;
import com.readyvery.readyverydemo.src.refreshtoken.RefreshTokenService;
import com.readyvery.readyverydemo.src.user.UserServiceFacade;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final JwtConfig jwtConfig;
	private final RefreshTokenService refreshTokenServiceImpl;
	private final UserServiceFacade userServiceFacade;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException, ServletException {

		log.info("=== OAuth2 로그인 성공 핸들러 시작 ===");
		log.info("요청 URI: {}", request.getRequestURI());
		log.info("인증 객체: {}", authentication.getClass().getSimpleName());

		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
			log.info("OAuth2 사용자 정보 - 이메일: {}, 이름: {}", 
				oAuth2User.getEmail(), oAuth2User.getName());

			// TODO : 아래 줄 예외 테스트 진행 필요
			UserInfo userInfo = userServiceFacade.getUserInfoByEmail(
				oAuth2User.getEmail()); // 사용자 정보가 없으면 예외 발생 (회원가입 페이지로 리다이렉트하기 위함

			log.info("DB에서 조회된 사용자 정보 - ID: {}, 역할: {}", userInfo.getId(), userInfo.getRole());

			loginSuccess(response, oAuth2User, userInfo.getRole()); // 로그인에 성공한 경우 access, refresh 토큰 생성
			
			// User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
			if (userInfo.getRole() == Role.GUEST) {
				log.info("GUEST 사용자 - 회원가입 페이지로 리다이렉트: {}", jwtConfig.getGuestFrontendUrl());
				response.sendRedirect(jwtConfig.getGuestFrontendUrl()); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

			} else {
				log.info("일반 사용자 - 메인 페이지로 리다이렉트: {}", jwtConfig.getUserFrontendUrl());
				response.sendRedirect(jwtConfig.getUserFrontendUrl());
			}

			log.info("=== OAuth2 로그인 성공 핸들러 완료 ===");

		} catch (Exception e) {
			log.error("=== OAuth2 로그인 성공 핸들러 실패 ===");
			log.error("에러 메시지: {}", e.getMessage());
			log.error("에러 스택트레이스: ", e);
			throw e;
		}

	}

	// TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, Role role) throws IOException {
		log.info("토큰 생성 시작 - 사용자: {}, 역할: {}", oAuth2User.getEmail(), role);
		
		String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
		String refreshToken = jwtService.createRefreshToken();
		
		log.info("토큰 생성 완료 - AccessToken 길이: {}, RefreshToken 길이: {}", 
			accessToken.length(), refreshToken.length());

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken, role);
		log.info("응답 헤더에 토큰 전송 완료");
		
		// RefreshTokenRepository를 직접 사용하지 말고 반드시 RefreshTokenService를 통해서만 접근하세요. (key-value 방식으로 변경됨)
		refreshTokenServiceImpl.saveRefreshTokenInRedis(oAuth2User.getEmail(), refreshToken);
		log.info("RefreshToken Redis 저장 완료");
	}

}
