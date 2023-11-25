package com.readyvery.readyverydemo.src.event.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainEventDto {
	private String imgUrl;
	private String redirectUrl;
}
