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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "INFOTAINMENT")
@AllArgsConstructor
@Slf4j
public class CeoMetaInfo extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ceo_meta_idx")
	private Long id;

	@Column(nullable = false, length = 45)
	private String storeName;

	@Column(nullable = false, length = 100)
	private String storeAddress;

	@Column(nullable = false, length = 45)
	private String registrationNumber;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String businessLicenseFileName;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String businessReportFileName;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String identityCardFileName;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String bankAccountFileName;

	// 입점신청 사장님 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ceo_idx")
	private CeoInfo ceoInfo;

}
