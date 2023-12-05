package com.readyvery.readyverydemo.src.order.dto;

import lombok.Getter;

@Getter
public class TosspaymentDto {
	private String message;
	private String paymentKey;
	private String type;
	private String orderId;
	private String orderName;
	private String mid;
	private String method;
	private String currency;
	private Long totalAmount;
	private Long balanceAmount;
	private Long suppliedAmount;
	private String status;
	private String requestedAt;
	private String approvedAt;
	private String lastTransactionKey;
	private Long vat;
	private Long taxFreeAmount;
	private Long taxExemptionAmount;
	private Object cancels;  // JSON 문자열로 변경
	private Object card;     // JSON 문자열로 변경
	private Object receipt;  // JSON 문자열로 변경
	private Object checkout; // JSON 문자열로 변경
	private Object easyPay;  // JSON 문자열로 변경
	private String country;
	private Object failure;  // JSON 문자열로 변경
	private Object discount; // JSON 문자열로 변경
	private Object virtualAccount; // JSON 문자열로 변경
	private Object transfer; // JSON 문자열로 변경
	private Object cashReceipt; // JSON 문자열로 변경
	private Object cashReceipts; // JSON 문자열로 변경
}
