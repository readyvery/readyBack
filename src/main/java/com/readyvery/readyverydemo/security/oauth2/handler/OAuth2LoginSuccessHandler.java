package com.readyvery.readyverydemo.security.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.security.jwt.service.JwtService;
import com.readyvery.readyverydemo.security.oauth2.CustomOAuth2User;

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
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws
		IOException, ServletException {
		log.info("OAuth2 Login 성공!");
		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

			loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
			log.info("OAuth2 Login 성공! - 로그인 성공 후 리다이렉트");
			response.sendRedirect(jwtService.getFrontendUrl());
		} catch (Exception e) {
			throw e;
		}

	}

	// TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
		String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
		String refreshToken = jwtService.createRefreshToken();

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

		// Redirect to the sign-up page
		//response.sendRedirect("http://localhost:3000/login");
	}

}
