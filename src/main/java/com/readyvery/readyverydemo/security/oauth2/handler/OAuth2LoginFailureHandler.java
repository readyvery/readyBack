package com.readyvery.readyverydemo.security.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException,
		ServletException {
		
		log.error("=== OAuth2 로그인 실패 핸들러 시작 ===");
		log.error("요청 URI: {}", request.getRequestURI());
		log.error("요청 파라미터: {}", request.getQueryString());
		log.error("User-Agent: {}", request.getHeader("User-Agent"));
		log.error("Referer: {}", request.getHeader("Referer"));
		
		log.error("인증 예외 타입: {}", exception.getClass().getSimpleName());
		log.error("인증 예외 메시지: {}", exception.getMessage());
		log.error("인증 예외 스택트레이스: ", exception);
		
		// 원인별 상세 정보 추가
		if (exception.getCause() != null) {
			log.error("인증 예외 원인: {}", exception.getCause().getMessage());
			log.error("인증 예외 원인 스택트레이스: ", exception.getCause());
		}
		
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("소셜 로그인 실패! 서버 로그를 확인해주세요.");
		
		log.error("=== OAuth2 로그인 실패 핸들러 완료 ===");
	}
}

