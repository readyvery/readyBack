package com.readyvery.readyverydemo.src.coupon;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.coupon.dto.CouponsRes;

public interface CouponService {
	CouponsRes getCoupon(CustomUserDetails userDetails);
}
