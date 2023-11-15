package com.readyvery.readyverydemo.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
	STORE_NOT_FOUND(404, "Store does not exists."),
	FOODY_NOT_FOUND(404, "Foody does not exists.");

	private int status;
	private String message;

	ExceptionCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
