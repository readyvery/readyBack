package com.readyvery.readyverydemo.src.store;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.repository.StoreRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceFacade {
	private final StoreRepository storeRepository;

	public Store getStoreById(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));
	}
}
