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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CART_ITEM")
@AllArgsConstructor
@Slf4j
public class CartItem extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_idx")
	private Long id;

	// 장바구니 아이템 수량
	@Column(nullable = false)
	private Long count;

	// 장바구니 메뉴 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_idx")
	private Foodie foodie;

	// 장바구니 아이템 장바구니 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_idx")
	private Cart cart;

	// 장바구니 아이템 - 장바구니 옵션 연관관계 매핑
	@OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL)
	private List<CartOption> cartOptions = new ArrayList<CartOption>();

}
