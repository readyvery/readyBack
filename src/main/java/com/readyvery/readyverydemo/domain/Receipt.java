package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_idx")
	private Order order;
}
