package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionCategoryDto {
	private String name;
	private Boolean essential;
	private List<FoodyOptionDto> options;
}
