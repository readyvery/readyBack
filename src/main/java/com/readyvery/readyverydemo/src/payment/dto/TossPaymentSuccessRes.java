package com.readyvery.readyverydemo.src.payment.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public class TossPaymentSuccessRes {
	private String orderId;
	private String orderName;
	private String method;
	private Long totalAmount;
	private String status;
	private LocalDateTime requestedAt;
	private LocalDateTime approvedAt;

}
