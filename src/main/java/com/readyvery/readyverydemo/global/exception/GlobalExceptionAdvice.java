package com.readyvery.readyverydemo.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<ErrorResponse.Default> handleException(BusinessLogicException exception) {
		final ErrorResponse.Default response = ErrorResponse.of(exception.getExceptionCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}
