package com.readyvery.readyverydemo.src.store;

import com.readyvery.readyverydemo.src.store.dto.StoreDetailRes;
import com.readyvery.readyverydemo.src.store.dto.StoreMenuRes;

public interface StoreService {
	StoreDetailRes getStoreDetail(Long storeId);

	StoreMenuRes getStoreMenu(Long storeId);
}
