package com.readyvery.readyverydemo.src.user.dto;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;

@Component
public class UserMapper {
	public UserAuthRes userInfoToUserAuthRes(CustomUserDetails userDetails) {
		return UserAuthRes.builder()
			.id(userDetails.getId())
			.email(userDetails.getEmail())
			.auth(userDetails.isEnabled())
			.admin(false)
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
