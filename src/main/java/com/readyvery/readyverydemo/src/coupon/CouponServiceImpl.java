package com.readyvery.readyverydemo.src.coupon;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.coupon.dto.CouponMapper;
import com.readyvery.readyverydemo.src.coupon.dto.CouponsRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	private final CouponMapper couponMapper;
	private final UserRepository userRepository;

	@Override
	public CouponsRes getCoupon(CustomUserDetails userDetails) {
		UserInfo userInfo = getUserInfo(userDetails);
		return couponMapper.toCouponsRes(userInfo.getCoupons());
	}

	private UserInfo getUserInfo(CustomUserDetails userDetails) {
		return userRepository.findById(userDetails.getId()).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}
}
