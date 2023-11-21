package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CART_OPTION")
@AllArgsConstructor
@Slf4j
@Builder
public class CartOption extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_option_idx")
	private Long id;

	// 장바구니 옵션 - 메뉴 옵션 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_option_idx")
	private FoodieOption foodieOption;

	// 장바구니 옵션 - 장바구니 아이템 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_item_idx")
	private CartItem cartItem;

	// 장바구니 옵션 - 메뉴 카테고리 옵션 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_option_category_idx")
	private FoodieOptionCategory foodieOptionCategory;

}
