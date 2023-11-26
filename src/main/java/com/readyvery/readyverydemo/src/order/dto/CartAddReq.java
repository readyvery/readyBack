package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CartAddReq {
	private Long storeId;
	private Long foodieId;
	private List<Long> options;
	private Long count;
	private Long inout;
}
