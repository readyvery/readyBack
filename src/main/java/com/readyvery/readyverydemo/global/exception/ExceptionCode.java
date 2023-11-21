package com.readyvery.readyverydemo.global.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
	STORE_NOT_FOUND(404, "Store does not exists."),
	USER_NOT_FOUND(404, "User does not exists."),
	FOODY_NOT_FOUND(404, "Foody does not exists."),
	FOODY_NOT_IN_STORE(400, "Foody does not exists in store."),
	INVALID_OPTION_COUNT(400, "Invalid option count."),
	INVALID_OPTION(400, "Invalid option."),
	OPTION_NOT_FOUND(404, "Option does not exists."),
	CART_ITEM_NOT_FOUND(200, "Cart item does not exists."),
	CART_NOT_FOUND(404, "Cart does not exists."),
	ITEM_NOT_SAME_STORE(400, "Item is not same store."),
	TOSS_PAYMENT_SUCCESS_FAIL(400, "Toss payment success fail."),
	ORDER_NOT_FOUND(400, "Order does not exists."),
	TOSS_PAYMENT_AMOUNT_NOT_MATCH(400, "Toss payment amount not match."),
	ORDER_NOT_CURRENT(400, "Order is not current.");


	private int status;
	private String message;

	ExceptionCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
