package com.readyvery.readyverydemo.security.jwt.service.sendmanger;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.config.JwtConfig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenSendManager {
	private final JwtConfig jwtConfig;

	public void addTokenCookie(HttpServletResponse response, String name, String value, String path, int maxAge,
		boolean httpOnly) {
		log.info("토큰 쿠키 추가 - 이름: {}, Path: {}, MaxAge: {}, HttpOnly: {}", name, path, maxAge, httpOnly);
		
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(httpOnly);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);

		log.info("토큰 쿠키 응답에 추가 완료");
	}

	// public ResponseEntity<UserLoginSuccessRes> addTokenResponseBody(String accessToken, String refreshToken,
	// 	Role role) {
	// 	UserLoginSuccessRes userLoginSuccessRes = UserLoginSuccessRes.builder()
	// 		.success(true)
	// 		.message("로그인 성공")
	// 		.accessToken(accessToken)
	// 		.refreshToken(refreshToken)
	// 		.role(role)
	// 		.build();
	//
	// 	return ResponseEntity.ok()
	// 		.contentType(MediaType.APPLICATION_JSON)
	// 		.body(userLoginSuccessRes);
	// }
}
