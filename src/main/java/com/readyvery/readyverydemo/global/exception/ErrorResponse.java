package com.readyvery.readyverydemo.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ErrorResponse {
	// 비즈니스로직 예외: 지정된 Code로 생성할 경우
	public static ErrorResponse.Default of(ExceptionCode exceptionCode) {
		return new ErrorResponse.Default(exceptionCode.getStatus(), exceptionCode.getMessage());
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Default {
		private Integer status;
		private String message;
	}
}
