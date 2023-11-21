package com.readyvery.readyverydemo.src.board.dto;

import static com.readyvery.readyverydemo.global.Constant.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.ImgSize;
import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;

@Component
public class BoardMapper {

	public BoardRes toBoardRes(List<Store> stores) {

		if (stores.isEmpty()) {
			throw new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND);
		}
		return BoardRes.builder()
			.stores(stores.stream().map(this::toStoreDto).toList())
			.build();
	}

	public BoardSearchRes toBoardSearchRes(List<Store> stores) {

		if (stores.isEmpty()) {
			throw new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND);
		}
		return BoardSearchRes.builder()
			.stores(stores.stream().map(this::toStoreDto).toList())
			.build();
	}

	private StoreDto toStoreDto(Store store) {
		return StoreDto.builder()
			.idx(store.getId())
			.name(store.getName())
			.address(store.getAddress())
			.imgUrl(store.getImgs().stream()
				.filter(storeImg -> storeImg.getImgSize().equals(ImgSize.VERY_PICK_CAFE_BANNER))
				.findFirst()
				.map(storeImg -> IMG_URL + store.getEngName() + "/" + storeImg.getImgUrl())
				.orElse(null))
			.build();
	}
}
