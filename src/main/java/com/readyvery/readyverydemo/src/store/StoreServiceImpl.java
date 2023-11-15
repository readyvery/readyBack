package com.readyvery.readyverydemo.src.store;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.repository.StoreRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.src.store.dto.StoreDetailRes;
import com.readyvery.readyverydemo.src.store.dto.StoreMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
	private final StoreRepository storeRepository;
	private final StoreMapper storeMapper;

	@Override
	public StoreDetailRes getStoreDetail(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));
		return storeMapper.storeToStoreDetailRes(store);
	}
}
