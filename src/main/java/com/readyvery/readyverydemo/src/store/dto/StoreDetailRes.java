package com.readyvery.readyverydemo.src.store.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreDetailRes {
	private List<String> imgs;
	private String name;
	private String phone;
	private String address;
	private String openTime;
	private Boolean status;
}
