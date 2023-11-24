package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Builder
@Table(name = "COUPON")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Coupon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_idx")
	private Long id;

	// 사용 여부
	@Column(name = "used")
	private boolean isUsed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_detail_idx")
	private CouponDetail couponDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private UserInfo userInfo;

	@OneToOne(mappedBy = "coupon", fetch = FetchType.LAZY)
	private Order order;

}
