package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Builder
@Table(name = "ORDERS", indexes = {@Index(name = "idx_order_id", columnList = "orderId", unique = true)})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Slf4j
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_idx")
	private Long id;

	// 총 결제 가격
	@Column
	private Long amount;

	// 결제 키 값
	@Column
	private String paymentKey;

	// 주문 id
	@Column
	private String orderId;

	// 주문 명
	@Column
	private String orderName;

	// 토스 결제 가격
	@Column
	private Long totalAmount;

	// 결제 방법
	@Column
	private String method;

	// 주문 번호
	@Column
	private String orderNumber;

	// 주문 상태
	@Column
	@Enumerated(EnumType.STRING)
	private Progress progress;

	@Column
	private Boolean payStatus; // 결제 성공 실패 여부

	// 가게 아이템 연관 관계
	// @OneToMany(mappedBy = "order")
	// @Builder.Default
	// private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	// 가게 연관 관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_idx")
	private Store store;

	// 유저 연관 관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private UserInfo userInfo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_idx")
	private Cart cart;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_idx")
	private Coupon coupon;

	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	private Receipt receipt;
}
