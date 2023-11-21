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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Table(name = "FOODIE")
@Slf4j
public class Foodie extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "foodie_idx")
	private Long id;

	//식품 이름
	@Column(nullable = false)
	private String name;

	//식품 가격
	@Column(nullable = false)
	private Long price;

	//식품 이미지
	@Column(nullable = true)
	private String imgUrl;

	//식품 매진
	@Column(nullable = false, columnDefinition = "BOOLEAN default false")
	private boolean soldOut;

	//식품 카테고리
	// @Column(nullable = false)
	// private String category;

	//히트 메뉴
	@Column(nullable = false, columnDefinition = "BOOLEAN default false")
	private boolean hit;

	//메뉴 - 가게 연관관계 매핑
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "store_idx")
	// private Store store;

	//메뉴 - 메뉴 카테고리 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_category_idx")
	private FoodieCategory foodieCategory;

	//메뉴 - 식품 카테고리 연관관계 매핑
	@OneToMany(mappedBy = "foodie", cascade = CascadeType.ALL)
	private List<FoodieOptionCategory> foodieOptionCategory = new ArrayList<FoodieOptionCategory>();

	//메뉴 - 테이크 아웃 연관관계 매핑
	@OneToOne(mappedBy = "foodie", cascade = CascadeType.ALL)
	private TakeOut takeOut;

	//메뉴 - 장바구니 아이템 연관관계 매핑
	@OneToMany(mappedBy = "foodie", cascade = CascadeType.ALL)
	private List<CartItem> cartItems = new ArrayList<CartItem>();

	@OneToMany(mappedBy = "foodie", cascade = CascadeType.ALL)
	private List<CouponMenu> couponMenus = new ArrayList<CouponMenu>();

	@OneToMany(mappedBy = "foodie", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
}
