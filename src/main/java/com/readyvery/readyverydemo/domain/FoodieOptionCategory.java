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
@Table(name = "FOODIE_OPTION_CATEGORIE")
@AllArgsConstructor
@Slf4j
public class FoodieOptionCategory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "foodie_option_category_idx")
	private Long id;

	//식품 옵션 카테고리 이름
	@Column(nullable = false)
	private String name;

	//식품 선택 필수 여부
	@Column(nullable = false, columnDefinition = "BOOLEAN default false")
	private boolean required;

	//식품 옵션 카테고리 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_idx")
	private Foodie foodie;

	//식품 옵션 연관관계 매핑
	@OneToMany(mappedBy = "foodieOptionCategory", cascade = CascadeType.ALL)
	private List<FoodieOption> foodieOptions = new ArrayList<FoodieOption>();

	// 식품 옵션 카테고리 - 장바구니 옵션 연관관계 매핑
	@OneToMany(mappedBy = "foodieOptionCategory", cascade = CascadeType.ALL)
	private List<CartOption> cartOptions = new ArrayList<CartOption>();

}
