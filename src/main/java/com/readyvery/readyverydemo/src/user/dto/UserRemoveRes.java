package com.readyvery.readyverydemo.src.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRemoveRes {

	private String message;
	private boolean success;
}
