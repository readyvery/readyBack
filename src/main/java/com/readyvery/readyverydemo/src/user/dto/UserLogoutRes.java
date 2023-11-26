package com.readyvery.readyverydemo.src.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLogoutRes {
	private boolean success;
	private String message;
}
