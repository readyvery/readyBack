package com.readyvery.readyverydemo.src.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoRes {

	private String name;
	private String phone;
	private String email;
}
