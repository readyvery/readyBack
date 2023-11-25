package com.readyvery.readyverydemo.src.order.dto;

import com.readyvery.readyverydemo.domain.Progress;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentRes {
	private String name;
	private String orderNum;
	private Progress progress;
	private String orderName;
	private String estimatedTime;
}
