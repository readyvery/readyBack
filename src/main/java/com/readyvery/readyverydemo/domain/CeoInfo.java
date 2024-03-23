package com.readyvery.readyverydemo.domain;

import java.time.LocalDateTime;
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
import jakarta.persistence.ManyToOne;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CEO")
@Slf4j
public class CeoInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ceo_idx")
	private Long id;

	// 이메일
	@Column(nullable = false, length = 45)
	private String email;

	// 비밀번호
	@Column(nullable = false, length = 100)
	private String password;

	// 닉네임
	@Column(nullable = false)
	private String nickName;

	// 프로필 이미지
	@Column(nullable = true)
	private String imageUrl;

	// 전화번호
	@Column
	private String phone;

	// 계좌번호
	@Column
	private String accountNumber;

	// 유저 권한
	@Column(nullable = false, columnDefinition = "VARCHAR(10) default 'USER'")
	@Enumerated(EnumType.STRING)
	private Role role;

	// 소셜 로그인 타입
	@Column
	@Enumerated(EnumType.STRING)
	private SocialType socialType; // KAKAO, NAVER, GOOGLE

	// 소셜 로그인 타입의 식별자 값 (일반 로그인인 경우 null)
	@Column
	private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

	// 유저 상태
	@Column(nullable = false, columnDefinition = "BOOLEAN default true")
	private boolean status;

	// 리프레시 토큰
	@Column(columnDefinition = "TEXT")
	private String refreshToken;

	// 계정 삭제 요청일
	@Column
	private LocalDateTime deleteDate;

	// 마지막 로그인 일시
	@Column
	private LocalDateTime lastLoginDate;

	// 사장님 가게 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_idx")
	private Store store = null;

	// 사장님 입점신청서 연관관계 매핑
	@Builder.Default
	@OneToMany(mappedBy = "ceoInfo", cascade = CascadeType.ALL)
	private List<CeoMetaInfo> ceoMetaInfos = new ArrayList<CeoMetaInfo>();

}
