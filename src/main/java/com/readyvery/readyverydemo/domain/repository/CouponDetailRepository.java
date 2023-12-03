package com.readyvery.readyverydemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.CouponDetail;

public interface CouponDetailRepository extends JpaRepository<CouponDetail, Long> {
}
