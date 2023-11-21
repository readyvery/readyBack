package com.readyvery.readyverydemo.src.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FailDto {
	private String code;
	private String message;
}
