package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	// 이벤트 메인 이미지 url
	@Column(name = "main_img")
	private String mainImg;

	// 이벤트 진행 여부
	@Column(name = "is_active")
	private boolean isActive;
}
