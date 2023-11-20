package com.readyvery.readyverydemo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Progress {
	CANCEL("CANCEL", "주문 취소"),
	REQUEST("REQUEST", "토스 결제 요청"),
	ORDER("ORDER", "주문 접수"),
	MAKE("MAKE", "음식 제조 중"),
	COMPLETE("COMPLETE", "제조 완료"),
	PICKUP("PICKUP", "픽업 완료");

	private final String key;
	private final String value;
}
