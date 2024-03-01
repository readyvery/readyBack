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
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(httpOnly);
		cookie.setPath(path);
		cookie.setDomain(jwtConfig.getCookieDomain());
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);

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
