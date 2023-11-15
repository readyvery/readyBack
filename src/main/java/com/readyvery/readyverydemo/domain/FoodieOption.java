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
@Table(name = "FOODIE_OPTIONS")
@AllArgsConstructor
@Slf4j
public class FoodieOption extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "foodie_option_idx")
	private Long id;

	//식품 옵션 이름
	@Column(nullable = false)
	private String name;

	//식품 옵션 가격
	@Column(nullable = false)
	private Long price;

	//식품 옵션 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_option_category_idx")
	private FoodieOptionCategory foodieOptionCategory;

	// 식품 옵션 - 장바구니 옵션 연관관계 매핑
	@OneToMany(mappedBy = "foodieOption", cascade = CascadeType.ALL)
	private List<CartOption> cartOptions = new ArrayList<CartOption>();

}
