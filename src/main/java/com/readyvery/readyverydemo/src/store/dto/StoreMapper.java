package com.readyvery.readyverydemo.src.store.dto;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.StoreImg;

@Component
public class StoreMapper {
	public StoreDetailRes storeToStoreDetailRes(Store store) {
		return StoreDetailRes.builder()
			.imgs(store.getImgs().stream().map(StoreImg::getImgUrl).toList())
			.name(store.getName())
			.phone(store.getPhone())
			.address(store.getAddress())
			.openTime(store.getTime())
			.build();
	}
}
