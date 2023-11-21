package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptHistoryDto {
	private String dateTime;
	private String name;
	private String imgUrl;
	private String orderName;
	private Long amount;
	private String orderId;
}
