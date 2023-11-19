package com.readyvery.readyverydemo.security.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getNickName() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)account.get("profile");

		if (account == null || profile == null) {
			return null;
		}

		return (String)profile.get("nickname");
	}

	@Override
	public String getImageUrl() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)account.get("profile");

		if (account == null || profile == null) {
			return null;
		}

		return (String)profile.get("thumbnail_image_url");
	}

	@Override
	public String getEmail() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		return (String)account.get("email");
	}

	@Override
	public String getPhoneNumber() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		return (String)account.get("phone_number");
	}

	@Override
	public String getBirth() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		return (String)account.get("birthyear");
	}

	@Override
	public String getAge() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		return (String)account.get("age_range");
	}
}
