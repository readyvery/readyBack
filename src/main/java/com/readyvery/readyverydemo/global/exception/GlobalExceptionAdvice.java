package com.readyvery.readyverydemo.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<?> handleBusinessLogicException(BusinessLogicException exception) {
		// AUTH_ERROR 예외 처리
		if (exception.getExceptionCode() == ExceptionCode.AUTH_ERROR) {
			AuthErrorResponse response = AuthErrorResponse.builder().auth(false).build();
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		// 다른 BusinessLogicException 예외 처리
		final ErrorResponse.Default defaultResponse = ErrorResponse.of(exception.getExceptionCode());
		return new ResponseEntity<>(defaultResponse, HttpStatus.valueOf(defaultResponse.getStatus()));
	}
}
