package com.readyvery.readyverydemo.src.user.dto;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.UserInfo;

@Component
public class UserMapper {
	public UserAuthRes userInfoToUserAuthRes(UserInfo userInfo) {
		return UserAuthRes.builder()
			.id(userInfo.getId())
			.email(userInfo.getEmail())
			.name(userInfo.getNickName())
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
