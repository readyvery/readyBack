package com.readyvery.readyverydemo.src.coupon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Coupon;
import com.readyvery.readyverydemo.domain.CouponDetail;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.CouponDetailRepository;
import com.readyvery.readyverydemo.domain.repository.CouponRepository;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.coupon.dto.CouponIssueReq;
import com.readyvery.readyverydemo.src.coupon.dto.CouponIssueRes;
import com.readyvery.readyverydemo.src.coupon.dto.CouponMapper;
import com.readyvery.readyverydemo.src.coupon.dto.CouponsRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	private final CouponMapper couponMapper;
	private final UserRepository userRepository;
	private final CouponDetailRepository couponDetailRepository;
	private final CouponRepository couponRepository;

	@Override
	public CouponsRes getCoupon(CustomUserDetails userDetails) {
		UserInfo userInfo = getUserInfo(userDetails);
		return couponMapper.toCouponsRes(userInfo.getCoupons());
	}

	@Override
	public synchronized CouponIssueRes issueCoupon(CustomUserDetails userDetails, CouponIssueReq couponIssueReq) {
		CouponDetail couponDetail = getCouponDetail(couponIssueReq);
		verifyCouponDetail(couponDetail, couponIssueReq);

		UserInfo userInfo = getUserInfo(userDetails);
		Long issuedCouponCount = getIssuedCouponCount(userInfo, couponDetail);

		verifyCouponIssue(couponDetail, issuedCouponCount);

		userInfo.getCoupons().addAll(issueUserCoupon(userInfo, couponDetail, issuedCouponCount));
		userRepository.save(userInfo);

		return couponMapper.toCouponIssueRes();
	}

	private Long getIssuedCouponCount(UserInfo userInfo, CouponDetail couponDetail) {
		return couponRepository.countByCouponDetailIdAndUserInfoId(couponDetail.getId(), userInfo.getId());
	}

	private void verifyCouponDetail(CouponDetail couponDetail, CouponIssueReq couponIssueReq) {
		verifyCoupon(couponDetail);
		verifyCouponCode(couponDetail, couponIssueReq);
	}

	private List<Coupon> issueUserCoupon(UserInfo userInfo, CouponDetail couponDetail, Long issuedCouponCount) {
		List<Coupon> coupons = new ArrayList<>();
		for (int i = 0; i < couponDetail.getCouponCount() - issuedCouponCount; i++) {
			coupons.add(Coupon.builder()
				.couponDetail(couponDetail)
				.userInfo(userInfo)
				.build());
		}
		return coupons;
	}

	private Long getCouponCount(UserInfo userDetails, CouponDetail couponDetail) {
		return userDetails.getCoupons().stream()
			.filter(coupon -> coupon.getCouponDetail().equals(couponDetail))
			.count();
	}

	private void verifyCouponIssue(CouponDetail couponDetail, Long issuedCouponCount) {
		verifyCouponIssueCount(issuedCouponCount, couponDetail);
	}

	private void verifyCouponIssueCount(Long issuedCouponCount, CouponDetail couponDetail) {
		if (issuedCouponCount >= couponDetail.getCouponCount()) {
			throw new BusinessLogicException(ExceptionCode.COUPON_ISSUE_COUNT_EXCEED);
		}
	}

	private void verifyCouponCode(CouponDetail couponDetail, CouponIssueReq couponIssueReq) {
		if (!couponDetail.getCouponCode().equals(couponIssueReq.getCouponCode())) {
			throw new BusinessLogicException(ExceptionCode.COUPON_CODE_NOT_MATCH);
		}
	}

	private void verifyCoupon(CouponDetail couponDetail) {
		if (!couponDetail.isActive()) {
			throw new BusinessLogicException(ExceptionCode.COUPON_NOT_ACTIVE);
		}
	}

	private CouponDetail getCouponDetail(CouponIssueReq couponIssueReq) {
		return couponDetailRepository.findById(couponIssueReq.getCouponId()).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.COUPON_NOT_FOUND)
		);
	}

	private UserInfo getUserInfo(CustomUserDetails userDetails) {
		return userRepository.findById(userDetails.getId()).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}
}
