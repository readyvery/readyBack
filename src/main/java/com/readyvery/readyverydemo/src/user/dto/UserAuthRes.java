package com.readyvery.readyverydemo.src.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAuthRes {
	private Long id;
	private String email;
}

