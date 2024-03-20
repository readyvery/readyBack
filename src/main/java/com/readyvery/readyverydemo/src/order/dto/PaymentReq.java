package com.readyvery.readyverydemo.src.order.dto;

import lombok.Getter;

@Getter
public class PaymentReq {
	private Long couponId;
	private Long cartId;
	private Long point;
}
