package com.readyvery.readyverydemo.security.oauth2.userinfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return (String) attributes.get("sub");
	}

	@Override
	public String getNickName() {
		return (String) attributes.get("name");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getImageUrl() {
		return (String) attributes.get("picture");
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
