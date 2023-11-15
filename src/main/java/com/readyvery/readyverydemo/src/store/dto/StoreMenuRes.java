package com.readyvery.readyverydemo.src.store.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreMenuRes {
	private List<MenuDto> menu;
}
