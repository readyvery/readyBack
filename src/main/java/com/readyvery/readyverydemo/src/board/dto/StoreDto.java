package com.readyvery.readyverydemo.src.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreDto {
	private Long idx;
	private String name;
	private String address;
	private String imgUrl;
}
