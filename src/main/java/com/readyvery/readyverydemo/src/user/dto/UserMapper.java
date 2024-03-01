package com.readyvery.readyverydemo.src.user.dto;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.UserInfo;

@Component
public class UserMapper {
	public UserAuthRes userInfoToUserAuthRes(boolean auth, UserInfo userInfo) {
		return UserAuthRes.builder()
			.id(userInfo.getId())
			.email(userInfo.getEmail())
			.auth(auth)
			.role(userInfo.getRole())
			.build();
	}

	public UserInfoRes userInfoToUserInfoRes(UserInfo userInfo) {
		return UserInfoRes.builder()
			.name(userInfo.getNickName())
			.phone(userInfo.getPhone())
			.email(userInfo.getEmail())
			.build();
	}

}
