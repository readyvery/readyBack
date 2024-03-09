package com.readyvery.readyverydemo.src.coupon.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Coupon;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponMapper {
	public CouponsRes toCouponsRes(List<Coupon> coupons) {
		return CouponsRes.builder()
			//filter로 isUsed가 false인 쿠폰만 가져옴
			.coupons(coupons.stream()
				.filter(coupon -> coupon.getIssueCount() - coupon.getUseCount() > 0)
				.map(this::toCouponDto)
				.toList())
			.build();
	}

	private CouponDto toCouponDto(Coupon coupon) {
		return CouponDto.builder()
			.couponId(coupon.getId())
			.couponName(coupon.getCouponDetail().getName())
			.description(coupon.getCouponDetail().getDescription())
			.expirationDate(coupon.getCouponDetail().getExpire())
			.publisher(coupon.getCouponDetail().getStore() == null
				? "레디베리" : coupon.getCouponDetail().getStore().getName())
			.salePrice(coupon.getCouponDetail().getSalePrice())
			.build();
	}

	public CouponIssueRes toCouponIssueRes() {
		return CouponIssueRes.builder()
			.message("쿠폰이 발급되었습니다.")
			.build();
	}
}
