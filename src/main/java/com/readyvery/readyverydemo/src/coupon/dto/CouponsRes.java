package com.readyvery.readyverydemo.src.coupon.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponsRes {
	private List<CouponDto> coupons;
}
