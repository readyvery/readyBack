package com.readyvery.readyverydemo.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.readyvery.readyverydemo.domain.Coupon;
import com.readyvery.readyverydemo.domain.CouponDetail;
import com.readyvery.readyverydemo.domain.UserInfo;

import jakarta.persistence.LockModeType;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	@Lock(LockModeType.OPTIMISTIC)
	Optional<Coupon> findById(Long id);

	Optional<Coupon> findByUserInfoAndCouponDetail(UserInfo userInfo, CouponDetail couponDetail);
}
