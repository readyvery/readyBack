package com.readyvery.readyverydemo.src.coupon.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponDto {
	private String couponName;
	private String publisher;
	private String description;
	private Long salePrice;
	private LocalDateTime expirationDate;
}
