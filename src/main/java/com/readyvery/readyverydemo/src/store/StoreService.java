package com.readyvery.readyverydemo.src.store;

import com.readyvery.readyverydemo.src.store.dto.StoreDetailRes;

public interface StoreService {
	StoreDetailRes getStoreDetail(Long storeId);
}
