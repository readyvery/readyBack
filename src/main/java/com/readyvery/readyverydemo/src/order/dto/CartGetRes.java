package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartGetRes {
	private String name;
	private String imgUrl;
	private Long totalPrice;
	private Long cartId;
	private List<CartDto> carts;
}
