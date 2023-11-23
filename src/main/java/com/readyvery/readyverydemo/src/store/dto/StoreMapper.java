package com.readyvery.readyverydemo.src.store.dto;

import static com.readyvery.readyverydemo.global.Constant.*;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.FoodieCategory;
import com.readyvery.readyverydemo.domain.ImgSize;
import com.readyvery.readyverydemo.domain.Store;

@Component
public class StoreMapper {
	public StoreDetailRes storeToStoreDetailRes(Store store) {
		return StoreDetailRes.builder()
			.imgs(store.getImgs()
				.stream()
				.filter(storeImg -> storeImg.getImgSize() == ImgSize.CAFE_BANNER)
				.map(storeImg -> IMG_URL + store.getEngName() + "/" + storeImg.getImgUrl())
				.toList())
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
			.imgUrl(foodie.getImgUrl() != null ? IMG_URL + foodie.getFoodieCategory().getStore().getEngName() + "/"
				+ foodie.getImgUrl() : null)
			.price(foodie.getPrice())
			.sale(foodie.getTakeOut() != null ? foodie.getTakeOut().getPrice() : null)
			.hit(foodie.isHit())
			.build();
	}

	public StoreEventRes storeToStoreEventRes(Store store) {
		return StoreEventRes.builder()
			.eventImgUrl("/images/" + store.getEngName() + "/" + store.getAdImgUrl())
			.build();
	}
}
