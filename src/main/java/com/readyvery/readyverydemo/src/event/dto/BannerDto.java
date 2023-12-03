package com.readyvery.readyverydemo.src.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerDto {
	private Long idx;
	private String bannerImg;
	private String couponCode;
}
