package com.readyvery.readyverydemo.src.store;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.repository.StoreRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.src.store.dto.StoreDetailRes;
import com.readyvery.readyverydemo.src.store.dto.StoreEventRes;
import com.readyvery.readyverydemo.src.store.dto.StoreMapper;
import com.readyvery.readyverydemo.src.store.dto.StoreMenuRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
	private final StoreRepository storeRepository;
	private final StoreMapper storeMapper;

	@Override
	public StoreDetailRes getStoreDetail(Long storeId) {
		Store store = getStore(storeId);
		return storeMapper.storeToStoreDetailRes(store);
	}

	private Store getStore(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));
	}

	@Override
	public StoreMenuRes getStoreMenu(Long storeId) {
		Store store = getStore(storeId);
		return storeMapper.storeToStoreMenuRes(store);
	}

	@Override
	public StoreEventRes getStoreEvent(Long storeId) {
		Store store = getStore(storeId);
		return storeMapper.storeToStoreEventRes(store);
	}

}
