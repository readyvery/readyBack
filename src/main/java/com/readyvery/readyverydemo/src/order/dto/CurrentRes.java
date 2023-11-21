package com.readyvery.readyverydemo.src.order.dto;

import com.readyvery.readyverydemo.domain.Progress;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentRes {
	private String name;
	private Long orderNum;
	private Progress progress;
	private String orderName;
	private String estimatedTime;
}
