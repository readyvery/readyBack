package com.readyvery.readyverydemo.src.point.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PointDto {
	private Boolean status;
	private String point;
	private String store;
	private String date;
}
