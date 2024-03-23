package com.readyvery.readyverydemo.src.store;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.src.store.dto.StoreDetailRes;
import com.readyvery.readyverydemo.src.store.dto.StoreEventRes;
import com.readyvery.readyverydemo.src.store.dto.StoreMenuRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/store")
public class StoreController {
	private final StoreService storeService;

	@GetMapping("/{storeId}")
	public ResponseEntity<StoreDetailRes> getStoreDetail(@PathVariable("storeId") Long storeId) {
		StoreDetailRes storeDetailRes = storeService.getStoreDetail(storeId);
		return new ResponseEntity<>(storeDetailRes, HttpStatus.OK);
	}

	@GetMapping("/{storeId}/menu")
	public ResponseEntity<StoreMenuRes> getStoreMenu(@PathVariable("storeId") Long storeId) {
		StoreMenuRes storeMenuRes = storeService.getStoreMenu(storeId);
		return new ResponseEntity<>(storeMenuRes, HttpStatus.OK);
	}

	@GetMapping("/{storeId}/event")
	public ResponseEntity<StoreEventRes> getStoreEvent(@PathVariable("storeId") Long storeId) {
		StoreEventRes storeEvent = storeService.getStoreEvent(storeId);
		return new ResponseEntity<>(storeEvent, HttpStatus.OK);
	}
}
