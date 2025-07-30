package com.readyvery.readyverydemo.security.oauth2.service;

import static com.readyvery.readyverydemo.config.OauthConfig.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readyvery.readyverydemo.domain.SocialType;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.security.oauth2.CustomOAuth2User;
import com.readyvery.readyverydemo.security.oauth2.OAuthAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("=== CustomOAuth2UserService.loadUser 시작 ===");
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		log.info("OAuth2 Provider: {}", registrationId);
		log.info("Client Registration ID: {}", userRequest.getClientRegistration().getClientId());
		log.info("Client Registration URI: {}", userRequest.getClientRegistration().getRedirectUri());
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		SocialType socialType = getSocialType(registrationId);
		log.info("변환된 SocialType: {}", socialType);
		
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는값
		log.info("UserName Attribute Name: {}", userNameAttributeName);
		
		Map<String, Object> attributes;
		/**
		 * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
		 * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
		 * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
		 * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
		 */
		if (registrationId.contains(APPLE_NAME)) {
			log.info("Apple 로그인 처리 시작");
			// Apple 로그인의 경우 JWT 토큰에서 사용자 정보를 디코드
			String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
			log.info("Apple ID Token 길이: {}", idToken.length());
			
			attributes = decodeJwtTokenPayload(idToken);
			attributes.put("id_token", idToken);

			log.info("Apple 사용자 정보 디코드 완료 - attributes: {}", attributes);
			log.info("Apple userNameAttributeName: {}", userNameAttributeName);

			// socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
			OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

			log.info("Apple extractAttributes: {}", extractAttributes);
			UserInfo createdUser = getUser(extractAttributes, socialType); // getUser() 메소드로 User 객체 생성 후 반환
			log.info("Apple 사용자 생성/조회 완료 - ID: {}, 이메일: {}", createdUser.getId(), createdUser.getEmail());
			
			// CustomOAuth2User 객체 생성
			return new CustomOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
				attributes,
				extractAttributes.getNameAttributeKey(),
				createdUser.getEmail(),
				createdUser.getRole()
			);
		} else {
			log.info("Google/Kakao 로그인 처리 시작");
			OAuth2User oAuth2User = delegate.loadUser(userRequest);

			/**
			 * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
			 * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
			 * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
			 */

			attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)
			log.info("OAuth2 사용자 정보 조회 완료 - attributes: {}", attributes);

			// socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
			OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);
			log.info("OAuthAttributes 생성 완료: {}", extractAttributes);

			UserInfo createdUser = getUser(extractAttributes, socialType); // getUser() 메소드로 User 객체 생성 후 반환
			log.info("사용자 생성/조회 완료 - ID: {}, 이메일: {}, 역할: {}", 
				createdUser.getId(), createdUser.getEmail(), createdUser.getRole());

			// DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
			CustomOAuth2User customUser = new CustomOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
				attributes,
				extractAttributes.getNameAttributeKey(),
				createdUser.getEmail(),
				createdUser.getRole()
			);
			
			log.info("=== CustomOAuth2UserService.loadUser 완료 ===");
			return customUser;
		}
	}

	private SocialType getSocialType(String registrationId) {
		if (KAKAO_NAME.equals(registrationId)) {
			return SocialType.KAKAO;
		} else if (APPLE_NAME.contains(registrationId)) {

			return SocialType.APPLE;
		}
		return SocialType.GOOGLE;
	}

	/**
	 * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
	 * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 saveUser()를 호출하여 회원을 저장한다.
	 */
	private UserInfo getUser(OAuthAttributes attributes, SocialType socialType) {
		log.info("사용자 조회/생성 시작 - SocialType: {}, SocialId: {}", 
			socialType, attributes.getOauth2UserInfo().getId());

		UserInfo findUser = userRepository.findBySocialTypeAndSocialId(socialType,
			attributes.getOauth2UserInfo().getId()).orElse(null);

		if (findUser == null) {
			log.info("신규 사용자 - 회원가입 진행");
			return saveUser(attributes, socialType);
		} else if (findUser.isStatus()) {
			log.info("기존 사용자 상태 업데이트 - ID: {}", findUser.getId());
			findUser.updateStatus(false);
			userRepository.save(findUser);
		} else {
			log.info("기존 사용자 로그인 - ID: {}, 이메일: {}", findUser.getId(), findUser.getEmail());
		}
		return findUser;
	}

	/**
	 * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
	 * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
	 */
	private UserInfo saveUser(OAuthAttributes attributes, SocialType socialType) {
		log.info("신규 사용자 저장 시작 - 이메일: {}, SocialType: {}", 
			attributes.getOauth2UserInfo().getEmail(), socialType);

		UserInfo createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
		UserInfo savedUser = userRepository.save(createdUser);
		
		log.info("신규 사용자 저장 완료 - ID: {}, 이메일: {}, 역할: {}", 
			savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
		
		return savedUser;
	}

	private Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
		Map<String, Object> jwtClaims = new HashMap<>();
		try {
			String[] parts = jwtToken.split("\\.");
			Base64.Decoder decoder = Base64.getUrlDecoder();

			byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
			String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> map = mapper.readValue(decodedString, Map.class);
			jwtClaims.putAll(map);

		} catch (JsonProcessingException e) {
			//        logger.error("decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);
		}
		return jwtClaims;
	}
}
