package com.readyvery.readyverydemo.src.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.coupon.dto.CouponIssueReq;
import com.readyvery.readyverydemo.src.coupon.dto.CouponIssueRes;
import com.readyvery.readyverydemo.src.coupon.dto.CouponsRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
	private final CouponService couponService;

	@GetMapping("")
	public ResponseEntity<CouponsRes> getCoupon(@AuthenticationPrincipal CustomUserDetails userDetails) {
		CouponsRes result = couponService.getCoupon(userDetails);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	//쿠폰 발급
	@PostMapping("")
	public ResponseEntity<CouponIssueRes> issueCoupon(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody CouponIssueReq couponIssueReq) {
		CouponIssueRes result = couponService.issueCoupon(userDetails, couponIssueReq);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
