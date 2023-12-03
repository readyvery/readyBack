package com.readyvery.readyverydemo.security.jwt.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

	/**
	 * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
	 *
	 */
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String EMAIL_CLAIM = "email";

	private static final String USER_NUMBER = "userNumber";
	private final UserRepository userRepository;
	@Value("${jwt.secretKey}")
	private String secretKey;
	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;
	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;
	@Value("${jwt.access.cookie}")
	private String accessCookie;
	@Value("${jwt.refresh.cookie}")
	private String refreshCookie;
	@Value("${jwt.redirect-uri}")
	private String frontendUrl;
	@Value("${jwt.access.cookie.domain}")
	private String accessCookieDomain;
	@Value("${jwt.refresh.cookie.domain}")
	private String refreshCookieDomain;

	/**
	 * AccessToken 생성 메소드
	 */
	public String createAccessToken(String email) {
		UserInfo userInfo = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
		Instant now = Instant.now();
		Instant expirationTime = now.plus(accessTokenExpirationPeriod, ChronoUnit.SECONDS);

		return JWT.create() // JWT 토큰을 생성하는 빌더 반환
			.withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
			.withExpiresAt(Date.from(expirationTime)) // 토큰 만료 시간 설정

			//클레임으로는 저희는 email 하나만 사용합니다.
			//추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
			//추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
			.withClaim(EMAIL_CLAIM, email)
			.withClaim(USER_NUMBER, userInfo.getId())
			.sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
	}

	/**
	 * RefreshToken 생성
	 * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
	 */
	public String createRefreshToken() {
		Instant now = Instant.now();
		Instant expirationTime = now.plus(refreshTokenExpirationPeriod, ChronoUnit.SECONDS);
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(Date.from(expirationTime))
			.sign(Algorithm.HMAC512(secretKey));
	}

	/**
	 * AccessToken + RefreshToken 헤더에 실어서 보내기
	 */
	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {

		response.setStatus(HttpServletResponse.SC_OK);

		setAccessTokenCookie(response, accessToken);
		setRefreshTokenCookie(response, refreshToken);

	}

	/**
	 * 쿠키에서 RefreshToken 추출
	 */
	public Optional<String> extractRefreshTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			return Arrays.stream(cookies)
				.filter(cookie -> refreshCookie.equals(cookie.getName())) // 올바른 필터링 조건
				.findFirst()
				.map(Cookie::getValue);
		}
		return Optional.empty();
	}

	/**
	 * 쿠키에서 AccessToken 추출
	 */
	public Optional<String> extractAccessTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			return Arrays.stream(cookies)
				.filter(cookie -> accessCookie.equals(cookie.getName())) // 올바른 필터링 조건
				.findFirst()
				.map(Cookie::getValue);
		}
		return Optional.empty();
	}

	/**
	 * AccessToken에서 Email 추출
	 * 추출 전에 JWT.require()로 검증기 생성
	 * verify로 AceessToken 검증 후
	 * 유효하다면 getClaim()으로 이메일 추출
	 * 유효하지 않다면 빈 Optional 객체 반환
	 */
	public Optional<String> extractEmail(String accessToken) {
		try {
			// 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
			return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
				.build() // 반환된 빌더로 JWT verifier 생성
				.verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
				.getClaim(EMAIL_CLAIM) // claim(Emial) 가져오기
				.asString());
		} catch (Exception e) {
			log.error("액세스 토큰이 유효하지 않습니다.");
			return Optional.empty();
		}
	}

	/**
	 * AccessToken 헤더 설정
	 */
	public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
		Cookie accessTokenCookie = new Cookie(accessCookie, accessToken); // 쿠키 생성
		//accessTokenCookie.setHttpOnly(true); // JavaScript가 쿠키를 읽는 것을 방지
		accessTokenCookie.setPath("/"); // 쿠키 경로 설정

		accessTokenCookie.setDomain(accessCookieDomain);

		// 필요한 경우 Secure 플래그 설정 (HTTPS에서만 쿠키 전송)
		//accessTokenCookie.setSecure(true);

		// 필요한 경우 동일한 사이트 속성 설정 (쿠키 전송에 대한 제한)
		// accessTokenCookie.setSameSite("Strict");

		// 도메인 설정
		//accessTokenCookie.setDomain("localhost");

		// 쿠키 만료 시간 설정 (예: 액세스 토큰 만료 시간과 같게 설정)
		accessTokenCookie.setMaxAge(accessTokenExpirationPeriod.intValue()); // 초 단위로 설정
		response.addCookie(accessTokenCookie); // 응답에 쿠키 추가
	}

	/**
	 * RefreshToken 헤더 설정
	 */
	public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		Cookie refreshTokenCookie = new Cookie(refreshCookie, refreshToken); // 쿠키 생성
		refreshTokenCookie.setHttpOnly(true); // JavaScript가 쿠키를 읽는 것을 방지
		refreshTokenCookie.setPath("/api/v1/refresh/token"); // 쿠키 경로 설정

		refreshTokenCookie.setDomain(refreshCookieDomain);

		// 필요한 경우 Secure 플래그 설정 (HTTPS에서만 쿠키 전송)
		// accessTokenCookie.setSecure(true);

		// 필요한 경우 동일한 사이트 속성 설정 (쿠키 전송에 대한 제한)
		// accessTokenCookie.setSameSite("Strict");

		// 쿠키 만료 시간 설정 (예: 액세스 토큰 만료 시간과 같게 설정)
		refreshTokenCookie.setMaxAge(refreshTokenExpirationPeriod.intValue()); // 초 단위로 설정
		response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가
	}

	/**
	 * RefreshToken DB 저장(업데이트)
	 */

	public void updateRefreshToken(String email, String refreshToken) {
		UserInfo user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NoSuchElementException("일치하는 회원이 없습니다."));

		user.updateRefresh(refreshToken);
		userRepository.save(user);
	}

	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}
}
