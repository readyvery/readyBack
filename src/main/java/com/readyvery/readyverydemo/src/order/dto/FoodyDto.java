package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class FoodyDto {
	private Long idx; // foodieId
	private Long count;
	private List<Long> options;
}
