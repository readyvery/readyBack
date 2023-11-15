package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodyDetailRes {
	private String name;
	private String imgUrl;
	private Long price;
	private List<OptionCategoryDto> category;
}
