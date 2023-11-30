package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartCountRes {
	private Long count;
}
