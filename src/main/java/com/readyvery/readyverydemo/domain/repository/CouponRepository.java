package com.readyvery.readyverydemo.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.readyvery.readyverydemo.domain.Coupon;

import jakarta.persistence.LockModeType;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	@Lock(LockModeType.OPTIMISTIC)
	Optional<Coupon> findById(Long id);

	Long countByCouponDetailIdAndUserInfoId(Long couponDetailId, Long userInfoId);
}
