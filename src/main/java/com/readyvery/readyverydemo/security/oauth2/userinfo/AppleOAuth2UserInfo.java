package com.readyvery.readyverydemo.security.oauth2.userinfo;

import java.util.Map;
import java.util.Optional;

public class AppleOAuth2UserInfo extends OAuth2UserInfo {

	public AppleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);

	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("sub"));
	}

	@Override
	public String getNickName() {
		// Map<String, Object> account = (Map<String, Object>)attributes.get("user");
		// Map<String, Object> profile = (Map<String, Object>)account.get("name");
		//
		// if (account == null || profile == null) {
		// 	return null;
		// }

		//return (String)profile.get("firstName");
		return (String)attributes.get("email");
	}

	@Override
	public String getImageUrl() {
		return "readyvery";
	}

	@Override
	public String getEmail() {
		return Optional.ofNullable((String)attributes.get("email"))
			.map(email -> email + "_apple")
			.orElse(null); // 여기에서는 null을 반환하지만, 다른 기본값으로 대체할 수도 있습니다.
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
