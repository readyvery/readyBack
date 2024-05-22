package com.readyvery.readyverydemo.src.order.dto;

import static com.readyvery.readyverydemo.domain.Progress.*;
import static com.readyvery.readyverydemo.global.Constant.*;
import static org.hibernate.type.descriptor.java.JdbcTimeJavaType.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Cart;
import com.readyvery.readyverydemo.domain.CartItem;
import com.readyvery.readyverydemo.domain.CartOption;
import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.FoodieOption;
import com.readyvery.readyverydemo.domain.FoodieOptionCategory;
import com.readyvery.readyverydemo.domain.ImgSize;
import com.readyvery.readyverydemo.domain.Order;
import com.readyvery.readyverydemo.domain.Point;
import com.readyvery.readyverydemo.domain.Receipt;
import com.readyvery.readyverydemo.src.order.config.TossPaymentConfig;
import com.readyvery.readyverydemo.src.point.PointPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	private final TossPaymentConfig tossPaymentConfig;
	private final PointPolicy pointPolicy;

	public FoodyDetailRes foodieToFoodyDetailRes(Foodie foodie, Long inout) {
		Long price = determinePrice(foodie, inout);

		return FoodyDetailRes.builder()
			.name(foodie.getName())
			.imgUrl(foodie.getImgUrl() != null ? IMG_URL + foodie.getFoodieCategory().getStore().getEngName() + "/"
				+ foodie.getImgUrl() : null)
			.price(price)
			.category(
				foodie.getFoodieOptionCategory()
					.stream()
					.map(this::foodieOptionCategoryToOptionCategoryDto)
					.toList())
			.build();
	}

	public Long determinePrice(Foodie foodie, Long inout) {
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
					.filter(options -> !options.getIsDelete())
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

	public CartGetRes cartToCartGetRes(Cart cart) {
		return CartGetRes.builder()
			.storeId(cart.getStore().getId())
			.cartId(cart.getId())
			.inOut(cart.getInOut())
			.isOpened(cart.getStore().isStatus())
			.name(cart.getStore().getName())
			.edit(!cart.getIsOrdered() && !cart.getIsDeleted())
			.imgUrl(cart.getStore()
				.getImgs()
				.stream()
				.filter(storeImg -> storeImg.getImgSize() == ImgSize.CAFE_LOGO)
				.map(storeImg -> IMG_URL + storeImg.getStore().getEngName() + "/" + storeImg.getImgUrl())
				.findFirst()
				.orElse(null))
			.carts(
				cart.getCartItems()
					.stream()
					.filter(cartItem -> !cartItem.getIsDeleted())
					.map(cartItem -> cartItemToCartDto(cartItem, cart.getInOut()))
					.toList())
			.totalPrice(
				cart.getCartItems()
					.stream()
					.filter(cartItem -> !cartItem.getIsDeleted())
					.mapToLong(cartItem -> cartItemTotalPrice(cartItem, cart.getInOut()))//
					.sum())
			.build();
	}

	private CartDto cartItemToCartDto(CartItem cartItem, Long inout) {
		return CartDto.builder()
			.idx(cartItem.getId())
			.name(cartItem.getFoodie().getName())
			.count(cartItem.getCount())
			// img
			.imgUrl(cartItem.getFoodie().getImgUrl() != null
				? IMG_URL + cartItem.getFoodie().getFoodieCategory().getStore().getEngName() + "/"
				+ cartItem.getFoodie().getImgUrl()
				: null)
			.totalPrice(cartItemsTotalPrice(cartItem, inout))
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
			.required(cartOption.getFoodieOption().getFoodieOptionCategory().isRequired())
			.name(cartOption.getFoodieOption().getName())
			.categoryName(cartOption.getFoodieOption().getFoodieOptionCategory().getName())
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

	private Long cartItemsTotalPrice(CartItem cartItem, Long inout) {
		Long optionsPriceSum = cartItem.getCartOptions()
			.stream()
			.mapToLong(cartOption -> cartOption.getFoodieOption().getPrice())
			.sum();

		return optionsPriceSum + determinePrice(cartItem.getFoodie(), inout);
	}

	public TosspaymentMakeRes orderToTosspaymentMakeRes(Order order) {
		return TosspaymentMakeRes.builder()
			.orderId(order.getOrderId())
			.orderName(order.getOrderName())
			.successUrl(tossPaymentConfig.getTossSuccessUrl())
			.failUrl(tossPaymentConfig.getTossFailUrl())
			.customerEmail(order.getUserInfo().getEmail())
			.customerName(order.getUserInfo().getNickName())
			.amount(order.getAmount())
			.build();
	}

	public Receipt tosspaymentDtoToReceipt(TosspaymentDto tosspaymentDto, Order order) {
		return Receipt.builder()
			.order(order)
			.type(tosspaymentDto.getType())
			.mid(tosspaymentDto.getMid())
			.currency(tosspaymentDto.getCurrency())
			.balanceAmount(tosspaymentDto.getBalanceAmount())
			.suppliedAmount(tosspaymentDto.getSuppliedAmount())
			.status(tosspaymentDto.getStatus())
			.requestedAt(tosspaymentDto.getRequestedAt())
			.approvedAt(tosspaymentDto.getApprovedAt())
			.lastTransactionKey(tosspaymentDto.getLastTransactionKey())
			.vat(tosspaymentDto.getVat())
			.taxFreeAmount(tosspaymentDto.getTaxFreeAmount())
			.taxExemptionAmount(tosspaymentDto.getTaxExemptionAmount())
			.cancels(tosspaymentDto.getCancels() != null ? tosspaymentDto.getCancels().toString() : null)
			.card(tosspaymentDto.getCard() != null ? tosspaymentDto.getCard().toString() : null)
			.receipt(tosspaymentDto.getReceipt() != null ? tosspaymentDto.getReceipt().toString() : null)
			.checkout(tosspaymentDto.getCheckout() != null ? tosspaymentDto.getCheckout().toString() : null)
			.easyPay(tosspaymentDto.getEasyPay() != null ? tosspaymentDto.getEasyPay().toString() : null)
			.country(tosspaymentDto.getCountry())
			.failure(tosspaymentDto.getFailure() != null ? tosspaymentDto.getFailure().toString() : null)
			.discount(tosspaymentDto.getDiscount() != null ? tosspaymentDto.getDiscount().toString() : null)
			.virtualAccount(
				tosspaymentDto.getVirtualAccount() != null ? tosspaymentDto.getVirtualAccount().toString() : null)
			.transfer(tosspaymentDto.getTransfer() != null ? tosspaymentDto.getTransfer().toString() : null)
			.cashReceipt(tosspaymentDto.getCashReceipt() != null ? tosspaymentDto.getCashReceipt().toString() : null)
			.cashReceipts(tosspaymentDto.getCashReceipts() != null ? tosspaymentDto.getCashReceipts().toString() : null)
			.build();
	}

	public FailDto makeFailDto(String code, String message) {
		return FailDto.builder()
			.code(code)
			.message(message)
			.build();
	}

	public HistoryRes ordersToHistoryRes(List<Order> orders) {
		return HistoryRes.builder()
			.receipts(
				orders
					.stream()
					.filter(order -> !order.getStore().isDeleted())
					.filter(order -> order.getProgress() == PICKUP
						|| order.getProgress() == COMPLETE
						|| order.getProgress() == FAIL
						|| order.getProgress() == CANCEL)
					.map(this::orderToReceiptHistoryDto)
					.toList())
			.build();
	}

	public HistoryRes ordersToFastOrderRes(List<Order> orders) {
		return HistoryRes.builder()
			.receipts(
				orders
					.stream()
					.filter(order -> !order.getStore().isDeleted())
					.filter(order -> order.getCart().getCreatedAt()
						// 데이터 변경 시점 이후의 데이터만 가져옴
						.isAfter(LocalDateTime.of(2023, 12, 6, 0, 0, 0)))
					.filter(order -> order.getProgress() == COMPLETE
						|| order.getProgress() == PICKUP)
					.map(this::orderToReceiptHistoryDto)
					.toList())
			.build();
	}

	public HistoryRes ordersToNewHistoryRes(List<Order> orders) {
		return HistoryRes.builder()
			.receipts(
				orders
					.stream()
					.filter(order -> !order.getStore().isDeleted())
					.filter(order -> order.getProgress() == ORDER
						|| order.getProgress() == MAKE)
					.map(this::orderToReceiptHistoryDto)
					.toList())
			.build();
	}

	private ReceiptHistoryDto orderToReceiptHistoryDto(Order order) {
		return ReceiptHistoryDto.builder()
			.dateTime(order.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
			.name(order.getStore().getName())
			.cartId(order.getCart().getId())
			.storeId(order.getStore().getId())
			.inOut(order.getInOut())
			.progress(order.getProgress())
			.imgUrl(order.getStore()
				.getImgs()
				.stream()
				.filter(storeImg -> storeImg.getImgSize() == ImgSize.CAFE_LOGO)
				.map(storeImg -> IMG_URL + storeImg.getStore().getEngName() + "/" + storeImg.getImgUrl())
				.findFirst()
				.orElse(null))
			.orderName(order.getOrderName())
			.amount(order.getTotalAmount())
			.orderId(order.getOrderId())
			.build();
	}

	public CurrentRes orderToCurrentRes(Order order) {
		return CurrentRes.builder()
			.inout(order.getInOut())
			.expectPoint(pointPolicy.calculatePoint(order.getAmount()))
			.cancels(order.getReceipt().getCancels())
			.name(order.getStore().getName())
			.orderNum(order.getOrderNumber())
			.progress(order.getProgress())
			.orderName(order.getOrderName())
			.estimatedTime(order.getEstimatedTime() != null
				? order.getEstimatedTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT)) : null)
			.build();
	}

	public DefaultRes tosspaymentDtoToCancelRes() {
		return DefaultRes.builder()
			.message("취소 성공")
			.build();
	}

	public HistoryDetailRes orderToHistoryDetailRes(Order order) {
		return HistoryDetailRes.builder()
			.inout(order.getInOut())
			.orderStatus(order.getProgress().toString())
			.orderNumber(order.getOrderNumber())
			.storeName(order.getStore().getName())
			.cancelReason(order.getReceipt() != null ? order.getReceipt().getCancels() : null)
			.orderTime(order.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
			.orderId(order.getOrderId())
			.storePhone(order.getStore().getPhone())
			.cart(cartToCartGetRes(order.getCart()))
			.salePrice(order.getTotalAmount() - order.getAmount())
			.method(order.getMethod())
			.build();
	}

	public CartCountRes cartToCartCountRes(Cart cart) {
		return CartCountRes.builder()
			.count(cart.getCartItems()
				.stream()
				.filter(cartItem -> !cartItem.getIsDeleted())
				.count())
			.build();
	}

	public PaySuccess tosspaymentDtoToPaySuccess(String message) {
		return PaySuccess.builder()
			.message(message)
			.build();
	}

	public Point orderToPoint(Order order) {
		return Point.builder()
			.order(order)
			.userInfo(order.getUserInfo())
			.point(order.getPoint())
			.isDeleted(false)
			.build();
	}
}
