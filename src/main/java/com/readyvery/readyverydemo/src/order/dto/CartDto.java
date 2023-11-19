package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartDto {
	private Long idx;
	private String name;
	private Long count;
	private Long totalPrice;
	private List<OptionDto> options;
}
