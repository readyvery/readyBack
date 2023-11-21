package com.readyvery.readyverydemo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImgSize {
	EVENT_BANNER(), // 이벤트 배너
	VERY_PICK_CAFE_BANNER(), // 카페 리스트 배너
	CAFE_LOGO(), // 카페 로고
	PICKUP_PROMOTION(), // 픽업 홍보 포스터
	CAFE_BANNER(), // 카페 상세 배너
	FOODY(), // 음료 사진
}
