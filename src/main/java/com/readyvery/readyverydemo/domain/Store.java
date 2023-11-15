package com.readyvery.readyverydemo.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "STORES")
@AllArgsConstructor
@Slf4j
public class Store extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_idx")
	private Long id;

	//가게 이름
	@Column(nullable = false)
	private String name;

	//가게 주소
	@Column(nullable = false)
	private String address;

	//가게 전화번호
	@Column(nullable = false)
	private String phone;

	//가게 영업시간
	@Column(nullable = false)
	private String time;

	//가게 영업중 여부
	@Column(nullable = false, columnDefinition = "BOOLEAN default true")
	private boolean status;

	//가게 광고 이미지
	@Column
	private String adImgUrl;

	//가게 등급
	@Column
	@Enumerated(EnumType.STRING)
	private Grade grade;

	//가게 사장님 연관관계 매핑
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ceo_idx")
	private CeoInfo ceoInfo;

	//가게 메뉴 연관관계 매핑
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Foodie> foodies = new ArrayList<>();

	//가게 장바구니 연관관계 매핑
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Cart> carts = new ArrayList<>();

}
