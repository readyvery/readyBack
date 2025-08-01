package com.readyvery.readyverydemo.security.jwt.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.security.jwt.service.JwtService;
import com.readyvery.readyverydemo.src.refreshtoken.RefreshTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

	private static final String NO_CHECK_URL = "/login"; // "/login"으로 들어오는 요청은 Filter 작동 X

	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final RefreshTokenService refreshTokenService;

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		log.info("JWT 인증 필터 처리 - URI: {}, Method: {}", requestURI, request.getMethod());
		
		// OAuth2 로그인 콜백 URL 확인
		if (requestURI.contains("/oauth2/") || requestURI.contains("/login/oauth2/")) {
			log.info("OAuth2 콜백 URL 감지: {}", requestURI);
		}
		
		if (request.getRequestURI().equals(NO_CHECK_URL)) {
			log.info("로그인 URL - JWT 필터 건너뛰기");
			filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
			return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
		}

		// 사용자 요청 헤더에서 RefreshToken 추출
		// -> RefreshToken이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
		// 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청한 경우밖에 없다.
		// 따라서, 위의 경우를 제외하면 추출한 refreshToken은 모두 null
		String refreshToken = jwtService.extractRefreshToken(request)
			.filter(jwtService::isTokenValid)
			.orElse(null);

		// 리프레시 토큰이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
		// RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단 후,
		// 일치한다면 AccessToken을 재발급해준다.
		if (refreshToken != null) {

			checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
			return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
		}

		// RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
		// AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
		// AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
		if (refreshToken == null) {
			checkAccessTokenAndAuthentication(request, response, filterChain);
		}
	}

	/**
	 *  [리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드]
	 *  파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면
	 *  JwtService.createAccessToken()으로 AccessToken 생성,
	 *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
	 *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
	 */
	public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
		// RefreshToken으로 이메일을 찾고, 해당 이메일로 사용자 조회
		String email = refreshTokenService.findEmailByRefreshToken(refreshToken);
		if (email != null) {
			userRepository.findByEmail(email)
				.ifPresent(user -> {
					String reIssuedRefreshToken = reIssueRefreshToken(user);
					jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()),
						reIssuedRefreshToken, user.getRole());
				});
		}
	}

	/**
	 * [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드]
	 * jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
	 * DB에 재발급한 리프레시 토큰 업데이트 후 Flush
	 */
	private String reIssueRefreshToken(UserInfo userInfo) {
		String reIssuedRefreshToken = jwtService.createRefreshToken();
		refreshTokenService.saveRefreshTokenInRedis(userInfo.getEmail(), reIssuedRefreshToken);
		return reIssuedRefreshToken;
	}

	/**
	 * [액세스 토큰 체크 & 인증 처리 메소드]
	 * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
	 * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
	 * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
	 * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
	 * 그 후 다음 인증 필터로 진행
	 */
	public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.ifPresent(accessToken -> jwtService.extractEmail(accessToken)
				.ifPresent(email -> userRepository.findByEmail(email)
					.ifPresent(user -> saveAuthentication(user, accessToken))));

		filterChain.doFilter(request, response);
	}

	/**
	 * [인증 허가 메소드]
	 * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
	 *
	 * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
	 * UsernamePasswordAuthenticationToken의 파라미터
	 * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
	 * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
	 * 3. Collection < ? extends GrantedAuthority>로,
	 * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
	 * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
	 *
	 * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
	 * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
	 */
	public void saveAuthentication(UserInfo myUser, String accessToken) {

		CustomUserDetails userDetailsUser = CustomUserDetails.builder()
			.id(myUser.getId())
			.email(myUser.getEmail())
			.password("readyvery")
			.accessToken(accessToken)
			.authorities(Collections.singletonList(new SimpleGrantedAuthority(myUser.getRole().toString())))
			.build();

		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetailsUser, null,
				authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
