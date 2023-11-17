package com.readyvery.readyverydemo.src.order.dto;

import static com.readyvery.readyverydemo.global.Constant.*;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.CartItem;
import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.FoodieOption;
import com.readyvery.readyverydemo.domain.FoodieOptionCategory;

@Component
public class OrderMapper {
	public FoodyDetailRes foodieToFoodyDetailRes(Foodie foodie, Long inout) {
		Long price = determinePrice(foodie, inout);

		return FoodyDetailRes.builder()
			.name(foodie.getName())
			.imgUrl(foodie.getImgUrl())
			.price(price)
			.category(
				foodie.getFoodieOptionCategory()
					.stream()
					.map(this::foodieOptionCategoryToOptionCategoryDto)
					.toList())
			.build();
	}

	private Long determinePrice(Foodie foodie, Long inout) {
		if (Objects.equals(inout, EAT_IN)) {
			return foodie.getPrice();
		} else if (Objects.equals(inout, TAKE_OUT)) {
			return foodie.getTakeOut() != null ? foodie.getTakeOut().getPrice() : foodie.getPrice();
		}
		// inout이 EAT_IN도 TAKE_OUT도 아닌 경우, null을 반환
		return null;
	}

	private OptionCategoryDto foodieOptionCategoryToOptionCategoryDto(FoodieOptionCategory category) {
		return OptionCategoryDto.builder()
			.name(category.getName())
			.essential(category.isRequired())
			.options(
				category.getFoodieOptions()
					.stream()
					.map(this::foodyOptionToOptionDto)
					.toList())
			.build();
	}

	private FoodyOptionDto foodyOptionToOptionDto(FoodieOption option) {
		return FoodyOptionDto.builder()
			.idx(option.getId())
			.name(option.getName())
			.price(option.getPrice())
			.build();
	}

	public CartAddRes cartToCartAddRes(CartItem cartItem) {
		return CartAddRes.builder()
			.cartItemId(cartItem.getId())
			.build();
	}

	public CartEidtRes cartToCartEditRes(CartItem cartItem) {
		return CartEidtRes.builder()
			.idx(cartItem.getId())
			.count(cartItem.getCount())
			.build();
	}
}
