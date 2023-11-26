package com.readyvery.readyverydemo.domain;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CART")
@AllArgsConstructor
@Slf4j
@Builder
@Setter
public class Cart extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_idx")
	private Long id;

	@Column(nullable = false)
	private Long inout;

	// 장바구니 유저 연관관계 매핑

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private UserInfo userInfo;

	// 장바구니 가게 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_idx")
	private Store store;

	@Column
	@Builder.Default
	private Boolean isOrdered = false;

	@Column
	@Builder.Default
	private Boolean isDeleted = false;

	@Builder.Default
	@OneToMany(mappedBy = "cart")
	private List<Order> orders = new ArrayList<Order>();

	// 장바구니 장바구니 아이템 연관관계 매핑
	@Builder.Default
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<CartItem> cartItems = new ArrayList<CartItem>();

}
