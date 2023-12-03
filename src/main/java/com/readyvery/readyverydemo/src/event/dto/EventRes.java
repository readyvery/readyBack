package com.readyvery.readyverydemo.src.event.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventRes {
	private List<BannerDto> banners;
}
