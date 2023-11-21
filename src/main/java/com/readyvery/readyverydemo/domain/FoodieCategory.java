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
@Table(name = "FOODIE_CATEGORY")
@AllArgsConstructor
@Slf4j
public class FoodieCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "foodie_category_idx")
	private Long id;

	@Column(nullable = false)
	private String name;

	//메뉴 - 가게 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_idx")
	private Store store;

	@OneToMany(mappedBy = "foodieCategory", cascade = CascadeType.ALL)
	private List<Foodie> foodies = new ArrayList<>();
}
