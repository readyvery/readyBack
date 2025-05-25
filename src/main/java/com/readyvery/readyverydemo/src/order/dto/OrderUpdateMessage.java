package com.readyvery.readyverydemo.src.order.dto;

import lombok.Data;

@Data
public class OrderUpdateMessage {
	private String orderId;  // 주문 식별자
	private Long storeId;    // 가게의 번호
	private String status;   // 주문 상태 (ORDER, MAKE, COMPLETE 등)
	private String message;  // 기타 메시지
}
