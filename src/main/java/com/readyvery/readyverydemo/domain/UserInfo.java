package com.readyvery.readyverydemo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USER", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_social_type_id", columnList = "social_type, social_id")
  })
@AllArgsConstructor
@Slf4j
public class UserInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_idx")
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
	@Column
	private String birth;

	// 전화번호
	@Column
	private String phone;

	// 유저 권한
	@Column(nullable = false, columnDefinition = "VARCHAR(10) default 'USER'")
	@Enumerated(EnumType.STRING)
	private Role role;

	// 소셜 로그인 타입
	@Column(name = "social_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private SocialType socialType; // KAKAO, NAVER, GOOGLE

	// 소셜 로그인 타입의 식별자 값 (일반 로그인인 경우 null)
	@Column(name = "social_id", nullable = false)
	private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

	// 유저 상태
	@Column(nullable = false, columnDefinition = "BOOLEAN default true")
	private boolean status;



	// 계정 삭제 요청일
	@Column
	private LocalDateTime deleteDate;

	// 마지막 로그인 일시
	@Column
	private LocalDateTime lastLoginDate;

	//포인트
	@Column
	@ColumnDefault("0")
	private Long point;

	// 유저 장바구니 연관관계 매핑
	@Builder.Default
	@OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL)
	private List<Cart> carts = new ArrayList<Cart>();

	// 유저 주문 연관관계 매핑
	@OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Order> orders = new ArrayList<Order>();

	// 유저 쿠폰 연관관계 매핑
	@OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Coupon> coupons = new ArrayList<Coupon>();

	public void updateRemoveUserDate() {
		this.status = true;
		this.deleteDate = LocalDateTime.now();
	}

	public void updateStatus(boolean status) {
		this.status = status;
	}

	public void updatePhone(String phoneNumber) {
		this.phone = phoneNumber;
		this.role = Role.USER;
	}
}
