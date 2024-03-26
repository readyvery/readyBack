package com.readyvery.readyverydemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class OrderNumberSequence {
	@Id
	private Long id;

	@Column(name = "order_number")
	private Long orderNumber;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id") // Order 테이블과 조인하는 컬럼 지정
	private Store store;

	public void increaseOrderNumber() {
		this.orderNumber++;
	}
}
