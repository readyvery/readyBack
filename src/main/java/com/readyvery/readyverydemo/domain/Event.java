package com.readyvery.readyverydemo.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Builder
@Table(name = "EVENT")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Event extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_idx")
	private Long id;

	// 이벤트 베너 이미지 url
	@Column(name = "banner_img")
	private String bannerImg;

	// 이벤트 메인 이미지 url
	@Column(name = "main_img")
	private String mainImg;

	@Builder.Default
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private List<CouponDetail> coupons = new ArrayList<CouponDetail>();

}
