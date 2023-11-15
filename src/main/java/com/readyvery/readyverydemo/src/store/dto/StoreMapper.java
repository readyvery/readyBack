package com.readyvery.readyverydemo.src.store.dto;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.FoodieCategory;
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

	public StoreMenuRes storeToStoreMenuRes(Store store) {
		return StoreMenuRes.builder()
			.menu(store.getFoodieCategories()
				.stream()
				.map(this::categoryToMenuDto)
				.toList())
			.build();
	}

	private MenuDto categoryToMenuDto(FoodieCategory category) {
		return MenuDto.builder()
			.categoryId(category.getId())
			.category(category.getName())
			.menuItems(category.getFoodies()
				.stream()
				.map(this::foodieToMenuItems)
				.toList())
			.build();
	}

	private MenuItemDto foodieToMenuItems(Foodie foodie) {
		return MenuItemDto.builder()
			.foodyId(foodie.getId())
			.name(foodie.getName())
			.imgUrl(foodie.getImgUrl())
			.price(foodie.getPrice())
			.sale(foodie.getTakeOut() != null ? foodie.getTakeOut().getPrice() : null)
			.hit(foodie.isHit())
			.build();
	}
}
