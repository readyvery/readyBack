package com.readyvery.readyverydemo.src.order;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Cart;
import com.readyvery.readyverydemo.domain.CartItem;
import com.readyvery.readyverydemo.domain.CartOption;
import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.FoodieOption;
import com.readyvery.readyverydemo.domain.FoodieOptionCategory;
import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.CartItemRepository;
import com.readyvery.readyverydemo.domain.repository.CartOptionRepository;
import com.readyvery.readyverydemo.domain.repository.CartRepository;
import com.readyvery.readyverydemo.domain.repository.FoodieOptionRepository;
import com.readyvery.readyverydemo.domain.repository.FoodieRepository;
import com.readyvery.readyverydemo.domain.repository.StoreRepository;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.order.dto.CartAddReq;
import com.readyvery.readyverydemo.src.order.dto.CartAddRes;
import com.readyvery.readyverydemo.src.order.dto.CartEditReq;
import com.readyvery.readyverydemo.src.order.dto.CartEidtRes;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteReq;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteRes;
import com.readyvery.readyverydemo.src.order.dto.CartResetRes;
import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;
import com.readyvery.readyverydemo.src.order.dto.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartOptionRepository cartOptionRepository;
	private final FoodieRepository foodieRepository;
	private final FoodieOptionRepository foodieOptionRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final OrderMapper orderMapper;

	@Override
	public FoodyDetailRes getFoody(Long storeId, Long foodyId, Long inout) {
		Foodie foodie = foodieRepository.findById(foodyId).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.FOODY_NOT_FOUND)
		);
		return orderMapper.foodieToFoodyDetailRes(foodie, inout);
	}

	@Override
	public CartAddRes addCart(CustomUserDetails userDetails, CartAddReq cartAddReq) {
		UserInfo user = getUserInfo(userDetails);
		Store store = getStore(cartAddReq.getStoreId());
		Foodie foodie = getFoody(cartAddReq.getFoodieId());

		verifyFoodieInStore(store, foodie);
		verifyCartAddReq(foodie, cartAddReq);

		Cart cart = cartRepository.findByUserInfoAndIsDeletedFalse(user).orElseGet(() -> makeCart(user, store));
		verifyItemsInCart(cart, store);
		CartItem cartItem = makeCartItem(cart, foodie, cartAddReq.getCount());
		List<CartOption> cartOptions = cartAddReq.getOptions().stream()
			.map(option -> makeCartOption(cartItem, option))
			.toList();

		cartRepository.save(cart);
		cartItemRepository.save(cartItem);
		cartOptionRepository.saveAll(cartOptions);

		return orderMapper.cartToCartAddRes(cartItem);
	}

	private void verifyItemsInCart(Cart cart, Store store) {
		if (!cart.getStore().equals(store)) {
			throw new BusinessLogicException(ExceptionCode.ITEM_NOT_SAME_STORE);
		}
	}

	@Override
	public CartEidtRes editCart(CustomUserDetails userDetails, CartEditReq cartEditReq) {
		CartItem cartItem = getCartItem(cartEditReq.getIdx());

		verifyCartItem(cartItem, userDetails);

		editCartItem(cartItem, cartEditReq);
		cartItemRepository.save(cartItem);
		return orderMapper.cartToCartEditRes(cartItem);
	}

	@Override
	public CartItemDeleteRes deleteCart(CustomUserDetails userDetails, CartItemDeleteReq cartItemDeleteReq) {
		CartItem cartItem = getCartItem(cartItemDeleteReq.getIdx());

		verifyCartItem(cartItem, userDetails);

		deleteCartItem(cartItem);
		cartItemRepository.save(cartItem);
		return orderMapper.cartToCartItemDeleteRes(cartItem);
	}

	@Override
	public CartResetRes resetCart(CustomUserDetails userDetails) {
		UserInfo user = getUserInfo(userDetails);
		Cart cart = getCart(user);

		resetCartItem(cart);

		cartRepository.save(cart);
		return orderMapper.cartToCartResetRes(cart);
	}

	private void resetCartItem(Cart cart) {
		cart.setIsDeleted(true);
	}

	private Cart getCart(UserInfo user) {
		return cartRepository.findByUserInfoAndIsDeletedFalse(user).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND)
		);
	}

	private void deleteCartItem(CartItem cartItem) {
		cartItem.setIsDeleted(true);
	}

	private void editCartItem(CartItem cartItem, CartEditReq cartEditReq) {
		cartItem.setCount(cartEditReq.getCount());
	}

	private void verifyCartItem(CartItem cartItem, CustomUserDetails userDetails) {
		boolean isCartItemOfUser = cartItem.getCart().getUserInfo().getId().equals(userDetails.getId());
		if (!isCartItemOfUser) {
			throw new BusinessLogicException(ExceptionCode.CART_ITEM_NOT_FOUND);
		}
	}

	private CartItem getCartItem(Long idx) {
		return cartItemRepository.findById(idx).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.CART_ITEM_NOT_FOUND)
		);
	}

	private CartOption makeCartOption(CartItem cartItem, Long option) {
		FoodieOption foodieOption = getFoodieOption(option);
		return CartOption.builder()
			.cartItem(cartItem)
			.foodieOption(foodieOption)
			.build();
	}

	private FoodieOption getFoodieOption(Long option) {
		return foodieOptionRepository.findById(option).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.OPTION_NOT_FOUND)
		);
	}

	private CartItem makeCartItem(Cart cart, Foodie foodie, Long count) {
		return CartItem.builder()
			.cart(cart)
			.foodie(foodie)
			.count(count)
			.build();
	}

	private Cart makeCart(UserInfo user, Store store) {
		return Cart.builder()
			.userInfo(user)
			.store(store)
			.build();
	}

	private UserInfo getUserInfo(CustomUserDetails userDetails) {
		return userRepository.findById(userDetails.getId()).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}

	private Store getStore(Long storeId) {
		return storeRepository.findById(storeId).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND)
		);
	}

	private void verifyCartAddReq(Foodie foodie, CartAddReq cartAddReq) {
		verifyOption(foodie, cartAddReq.getOptions());
		verifyEssentialOption(foodie, cartAddReq.getOptions());
	}

	private Foodie getFoody(Long foodieId) {
		return foodieRepository.findById(foodieId).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.FOODY_NOT_FOUND)
		);
	}

	private void verifyFoodieInStore(Store store, Foodie foodie) {
		boolean isFoodieInStore = foodie.getFoodieCategory().getStore().equals(store);
		if (!isFoodieInStore) {
			throw new BusinessLogicException(ExceptionCode.FOODY_NOT_IN_STORE);
		}
	}

	private void verifyOption(Foodie foodie, List<Long> opotions) {
		// foodie안에 있는 옵션들을 Set으로 만들기
		Set<Long> optionSet = foodie.getFoodieOptionCategory().stream()
			.flatMap(foodieOptionCategory -> foodieOptionCategory.getFoodieOptions().stream())
			.map(FoodieOption::getId)
			.collect(Collectors.toSet());

		// 옵션들이 올바른지 확인
		opotions.stream()
			.filter(option -> !optionSet.contains(option))
			.findAny()
			.ifPresent(option -> {
				throw new BusinessLogicException(ExceptionCode.INVALID_OPTION);
			});
	}

	private void verifyEssentialOption(Foodie foodie, List<Long> options) {
		foodie.getFoodieOptionCategory().stream()
			.filter(FoodieOptionCategory::isRequired)
			.collect(Collectors.toSet()).stream()
			.filter(foodieOptionCategory -> foodieOptionCategory.getFoodieOptions().stream()
				.map(FoodieOption::getId)
				.filter(options::contains)
				.count() != 1)  // 필수 값이 1개가 아닌 경우
			.findAny()
			.ifPresent(foodieOptionCategory -> {
				throw new BusinessLogicException(ExceptionCode.INVALID_OPTION_COUNT);
			});
	}
}
