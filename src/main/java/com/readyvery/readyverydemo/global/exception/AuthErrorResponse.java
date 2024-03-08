package com.readyvery.readyverydemo.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthErrorResponse {
	private boolean auth;

}
