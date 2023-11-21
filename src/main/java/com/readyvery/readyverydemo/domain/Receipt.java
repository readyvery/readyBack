package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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
@Table(name = "RECEIPT")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Receipt {
	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "order_idx")
	private Order order;

	@Column
	private String type;

	@Column
	private String mid;

	@Column
	private String currency;

	@Column
	private Long balanceAmount;

	@Column
	private Long suppliedAmount;

	@Column
	private String status;

	@Column
	private String requestedAt;

	@Column
	private String approvedAt;

	@Column
	private String lastTransactionKey;

	@Column
	private Long vat;

	@Column
	private Long taxFreeAmount;

	@Column
	private Long taxExemptionAmount;

	// @Column(columnDefinition = "json")
	@Column
	private String cancels;

	// @Column(columnDefinition = "json")
	@Column
	private String card;

	// @Column(columnDefinition = "json")
	@Column
	private String receipt;

	// @Column(columnDefinition = "json")
	@Column
	private String checkout;

	// @Column(columnDefinition = "json")
	@Column
	private String easyPay;

	@Column
	private String country;

	// @Column(columnDefinition = "json")
	@Column
	private String failure;

	// @Column(columnDefinition = "json")
	@Column
	private String discount;

	// @Column(columnDefinition = "json")
	@Column
	private String virtualAccount;

	// @Column(columnDefinition = "json")
	@Column
	private String transfer;

	// @Column(columnDefinition = "json")
	@Column
	private String cashReceipt;

	// @Column(columnDefinition = "json")
	@Column
	private String cashReceipts;
}
