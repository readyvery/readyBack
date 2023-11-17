package com.readyvery.readyverydemo.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

	// 닉네임
	@Column(nullable = false)
	private String nickName;

	// 프로필 이미지
	@Column(nullable = false, columnDefinition = "TEXT")
	private String imageUrl;

	// 연령대
	@Column
	private String age;

	// 생일
	@Column(nullable = false)
	private String birth;

	// 전화번호
	@Column(nullable = false)
	private String phone;

	// 계좌번호
	@Column
	private String accountNumber;

	// 소셜 로그인 타입
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SocialType socialType; // KAKAO, NAVER, GOOGLE

	// 소셜 로그인 타입의 식별자 값 (일반 로그인인 경우 null)
	@Column(nullable = false)
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
	@OneToOne(mappedBy = "ceoInfo", fetch = LAZY)
	private Store store;

}
