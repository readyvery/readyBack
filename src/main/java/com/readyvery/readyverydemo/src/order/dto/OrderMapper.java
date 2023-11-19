package com.readyvery.readyverydemo.src.order.dto;

import static com.readyvery.readyverydemo.global.Constant.*;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Cart;
import com.readyvery.readyverydemo.domain.CartItem;
import com.readyvery.readyverydemo.domain.CartOption;
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
		// inout이 EAT_IN도 TAKE_OUT도 아닌 경우, 기본 값 반환
		return foodie.getPrice();
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

	public CartItemDeleteRes cartToCartItemDeleteRes(CartItem cartItem) {
		return CartItemDeleteRes.builder()
			.idx(cartItem.getId())
			.build();
	}

	public CartResetRes cartToCartResetRes(Cart cart) {
		return CartResetRes.builder()
			.idx(cart.getId())
			.build();
	}

	public CartGetRes cartToCartGetRes(Cart cart, Long inout) {
		return CartGetRes.builder()
			.name(cart.getStore().getName())
			.imgUrl(cart.getStore().getImgs().get(0).getImgUrl())
			.carts(
				cart.getCartItems()
					.stream()
					.map(cartItem -> cartItemToCartDto(cartItem, inout))
					.toList())
			.totalPrice(
				cart.getCartItems()
					.stream()
					.mapToLong(cartItem -> cartItemTotalPrice(cartItem, inout))
					.sum())
			.build();
	}

	private CartDto cartItemToCartDto(CartItem cartItem, Long inout) {
		return CartDto.builder()
			.idx(cartItem.getId())
			.name(cartItem.getFoodie().getName())
			.count(cartItem.getCount())
			.totalPrice(cartItemTotalPrice(cartItem, inout))
			.options(
				cartItem.getCartOptions()
					.stream()
					.map(this::cartOptionToOptionDto)
					.toList())
			.build();
	}

	private OptionDto cartOptionToOptionDto(CartOption cartOption) {
		return OptionDto.builder()
			.idx(cartOption.getId())
			.name(cartOption.getFoodieOption().getName())
			.price(cartOption.getFoodieOption().getPrice())
			.build();
	}

	private Long cartItemTotalPrice(CartItem cartItem, Long inout) {
		Long optionsPriceSum = cartItem.getCartOptions()
			.stream()
			.mapToLong(cartOption -> cartOption.getFoodieOption().getPrice())
			.sum();

		Long totalPrice = optionsPriceSum + determinePrice(cartItem.getFoodie(), inout);
		return totalPrice * cartItem.getCount();
	}

}
