package com.readyvery.readyverydemo.security.jwt.dto;

import com.readyvery.readyverydemo.domain.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginSuccessRes {
	private boolean success;
	private String message;
	private String accessToken;
	private String refreshToken;
	private Role role;
}
