package com.readyvery.readyverydemo.security.oauth2.userinfo;

import java.util.Map;

public class AppleOAuth2UserInfo extends OAuth2UserInfo {

	public AppleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
		System.out.println("attributes = " + attributes);
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getNickName() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("user");
		Map<String, Object> profile = (Map<String, Object>)account.get("name");

		if (account == null || profile == null) {
			return null;
		}

		return (String)profile.get("firstName");
	}

	@Override
	public String getImageUrl() {
		return "readyvery";
	}

	@Override
	public String getEmail() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("user");

		if (account == null) {
			return null;
		}

		return (String)account.get("email");
	}

	@Override
	public String getPhoneNumber() {
		return "readyvery";
	}

	@Override
	public String getBirth() {
		return "readyvery";
	}

	@Override
	public String getAge() {

		return "readyvery";
	}
}
