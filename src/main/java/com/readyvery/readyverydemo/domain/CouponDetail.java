package com.readyvery.readyverydemo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Builder
@Table(name = "COUPONDETAIL")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CouponDetail extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_detail_idx")
	private Long id;

	// 할인 가격
	@Column
	private Long salePrice;

	// 쿠폰 이름
	@Column
	private String name;

	// 만료일
	@Column
	private LocalDateTime expire;

	// 쿠폰 코드
	@Column(name = "coupon_code")
	private String couponCode;

	// 발급 갯수
	@Column(name = "coupon_count")
	private Long couponCount;

	// 발행처 레디베리 발행(null) / 사장님 발행(store_idx)
	@Column
	private Long publisher;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_idx")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_idx")
	private Foodie foodie;

	@Builder.Default
	@OneToMany(mappedBy = "couponDetail", cascade = CascadeType.ALL)
	private List<Coupon> coupons = new ArrayList<Coupon>();

}
