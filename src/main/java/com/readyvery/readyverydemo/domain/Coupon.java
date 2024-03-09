package com.readyvery.readyverydemo.domain;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Builder
@Table(name = "COUPON", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"coupon_detail_idx", "user_idx"})
})
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Coupon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_idx")
	private Long id;

	// 발급 갯수
	@Column(name = "issue_count")
	private int issueCount;

	// 사용 갯수
	@Column(name = "use_count")
	private Long useCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_detail_idx")
	private CouponDetail couponDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private UserInfo userInfo;

	@Version
	private Long version;

	@Builder.Default
	@OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<Order>();
}
