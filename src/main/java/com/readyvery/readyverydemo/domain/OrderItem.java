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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Builder
@Table(name = "ORDER_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_idx")
	private Long id;

	@Column
	private Long count;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_idx")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodie_idx")
	private Foodie foodie;

	@OneToMany(mappedBy = "orderItem")
	private List<OrderItemOption> orderItemOptions = new ArrayList<OrderItemOption>();

}
