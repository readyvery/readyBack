package com.readyvery.readyverydemo.src.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MenuItemDto {
	private Long foodyId;
	private String name;
	private String imgUrl;
	private Long price;
	private Long sale;
	private Boolean hit;
}
