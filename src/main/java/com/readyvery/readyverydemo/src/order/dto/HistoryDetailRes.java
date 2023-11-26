package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryDetailRes {
	private String orderStatus;
	private String storeName;
	private String orderTime;
	private String orderId;
	private String storePhone;
	private CartGetRes cart;
	private String salePrice;
	private String method;
	private Long cartId;
	private Long storeId;
	private Long inOut;
}
