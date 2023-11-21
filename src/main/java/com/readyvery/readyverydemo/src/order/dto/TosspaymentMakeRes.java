package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TosspaymentMakeRes {
	private String orderId;
	private String orderName;
	private String successUrl;
	private String failUrl;
	private String customerEmail;
	private String customerName;
	private Long amount;
}
