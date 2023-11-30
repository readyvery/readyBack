package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDto {
	private Long idx;
	private String name;
	private Long price;
	private Boolean required;
	private String categoryName;
}
