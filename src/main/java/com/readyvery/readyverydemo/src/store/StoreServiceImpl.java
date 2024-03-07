package com.readyvery.readyverydemo.src.store;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.src.store.dto.StoreDetailRes;
import com.readyvery.readyverydemo.src.store.dto.StoreEventRes;
import com.readyvery.readyverydemo.src.store.dto.StoreMapper;
import com.readyvery.readyverydemo.src.store.dto.StoreMenuRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
	private final StoreServiceFacade storeServiceFacade;
	private final StoreMapper storeMapper;

	@Override
	public StoreDetailRes getStoreDetail(Long storeId) {
		Store store = storeServiceFacade.getStoreById(storeId);
		return storeMapper.storeToStoreDetailRes(store);
	}

	@Override
	public StoreMenuRes getStoreMenu(Long storeId) {
		Store store = storeServiceFacade.getStoreById(storeId);
		return storeMapper.storeToStoreMenuRes(store);
	}

	@Override
	public StoreEventRes getStoreEvent(Long storeId) {
		Store store = storeServiceFacade.getStoreById(storeId);
		return storeMapper.storeToStoreEventRes(store);
	}

}
