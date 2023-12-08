package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryDetailRes {
	private Long inout;
	private String orderStatus;
	private String storeName;
	private String orderTime;
	private String orderId;
	private String storePhone;
	private CartGetRes cart;
	private Long salePrice;
	private String method;
	private String orderNumber;
	private Object cancelReason;
}
