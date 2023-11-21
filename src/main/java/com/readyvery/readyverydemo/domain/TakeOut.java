package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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
@Table(name = "TAKE_OUT")
@AllArgsConstructor
@Slf4j
public class TakeOut {

	@Id
	private Long id;

	//테이크 아웃 메뉴 연관관계 매핑
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "foodie_idx")
	private Foodie foodie;

	//테이크 아웃 가격
	@Column
	private Long price;

}
