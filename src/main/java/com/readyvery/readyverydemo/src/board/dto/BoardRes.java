package com.readyvery.readyverydemo.src.board.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRes {
	private List<StoreDto> stores;
}
