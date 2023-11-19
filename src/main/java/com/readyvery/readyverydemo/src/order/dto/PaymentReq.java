package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class PaymentReq {
	private Long storeId;
	private Long couponId;
	private Long inout;
	private List<FoodyDto> carts;
}
