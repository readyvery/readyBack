package com.readyvery.readyverydemo.security.config;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.security.exception.CustomAuthenticationEntryPoint;
import com.readyvery.readyverydemo.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.readyvery.readyverydemo.security.jwt.service.JwtService;
import com.readyvery.readyverydemo.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.readyvery.readyverydemo.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.readyvery.readyverydemo.security.oauth2.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// [PART 1]
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			// [PART 2]
			//== URL별 권한 관리 옵션 ==//
			.authorizeHttpRequests((authz) -> authz
				.requestMatchers(
					"/jwt-test",
					"/oauth2/**",
					"/login",
					"/api/v1/store/**"
				).permitAll() // 해당 요청은 인증이 필요함
				.anyRequest().authenticated() // 위를 제외한 나머지는 모두 허용
			)
			// [PART 3]
			//== 소셜 로그인 설정 ==//
			.oauth2Login(oauth2 -> oauth2
				.successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
				.failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)))

			// Custom Exception Handling
			.exceptionHandling(exceptionHandling ->
				exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint(new ObjectMapper()))
			);

		// [PART4]
		// 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
		// 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
		// 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
		http.addFilterBefore(jwtAuthenticationProcessingFilter(), LogoutFilter.class);

		return http.build();
	}

	@Bean
	public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(ObjectMapper objectMapper) {
		return new CustomAuthenticationEntryPoint(objectMapper);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	} // 패스워드 인코더

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("POST", "PATCH", "GET", "DELETE"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	} // CORS 설정

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	} // 정적 리소스 보안 필터 해제

	@Bean
	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService,
			userRepository);
		return jwtAuthenticationFilter;
	}
}
