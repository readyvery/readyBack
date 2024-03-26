package com.readyvery.readyverydemo.src.order;

import static com.readyvery.readyverydemo.global.Constant.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;

import com.readyvery.readyverydemo.domain.Cart;
import com.readyvery.readyverydemo.domain.CartItem;
import com.readyvery.readyverydemo.domain.CartOption;
import com.readyvery.readyverydemo.domain.Coupon;
import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.FoodieOption;
import com.readyvery.readyverydemo.domain.FoodieOptionCategory;
import com.readyvery.readyverydemo.domain.Order;
import com.readyvery.readyverydemo.domain.OrderNumberSequence;
import com.readyvery.readyverydemo.domain.Point;
import com.readyvery.readyverydemo.domain.Progress;
import com.readyvery.readyverydemo.domain.Receipt;
import com.readyvery.readyverydemo.domain.Store;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.CartItemRepository;
import com.readyvery.readyverydemo.domain.repository.CartOptionRepository;
import com.readyvery.readyverydemo.domain.repository.CartRepository;
import com.readyvery.readyverydemo.domain.repository.CouponRepository;
import com.readyvery.readyverydemo.domain.repository.FoodieOptionRepository;
import com.readyvery.readyverydemo.domain.repository.FoodieRepository;
import com.readyvery.readyverydemo.domain.repository.OrderNumberSequenceRepository;
import com.readyvery.readyverydemo.domain.repository.OrdersRepository;
import com.readyvery.readyverydemo.domain.repository.PointRepository;
import com.readyvery.readyverydemo.domain.repository.ReceiptRepository;
import com.readyvery.readyverydemo.domain.repository.StoreRepository;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.order.config.TossPaymentConfig;
import com.readyvery.readyverydemo.src.order.dto.CartAddReq;
import com.readyvery.readyverydemo.src.order.dto.CartAddRes;
import com.readyvery.readyverydemo.src.order.dto.CartCountRes;
import com.readyvery.readyverydemo.src.order.dto.CartEidtRes;
import com.readyvery.readyverydemo.src.order.dto.CartGetRes;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteRes;
import com.readyvery.readyverydemo.src.order.dto.CartResetRes;
import com.readyvery.readyverydemo.src.order.dto.CurrentRes;
import com.readyvery.readyverydemo.src.order.dto.FailDto;
import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;
import com.readyvery.readyverydemo.src.order.dto.HistoryDetailRes;
import com.readyvery.readyverydemo.src.order.dto.HistoryRes;
import com.readyvery.readyverydemo.src.order.dto.OrderMapper;
import com.readyvery.readyverydemo.src.order.dto.PaySuccess;
import com.readyvery.readyverydemo.src.order.dto.PaymentReq;
import com.readyvery.readyverydemo.src.order.dto.TossCancelReq;
import com.readyvery.readyverydemo.src.order.dto.TosspaymentDto;
import com.readyvery.readyverydemo.src.order.dto.TosspaymentMakeRes;
import com.readyvery.readyverydemo.src.point.PointServiceFacade;
import com.readyvery.readyverydemo.src.user.UserServiceFacade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartOptionRepository cartOptionRepository;
	private final FoodieRepository foodieRepository;
	private final FoodieOptionRepository foodieOptionRepository;
	private final UserRepository userRepository;
	private final UserServiceFacade userServiceFacade;
	private final StoreRepository storeRepository;
	private final OrderMapper orderMapper;
	private final TossPaymentConfig tosspaymentConfig;
	private final OrdersRepository ordersRepository;
	private final ReceiptRepository receiptRepository;
	private final CouponRepository couponRepository;
	private final PointRepository pointRepository;
	private final PointServiceFacade pointServiceFacade;
	private final OrderNumberSequenceRepository orderNumberSequenceRepository;

	@Override
	public FoodyDetailRes getFoody(Long storeId, Long foodyId, Long inout) {
		Foodie foodie = getFoody(foodyId);
		return orderMapper.foodieToFoodyDetailRes(foodie, inout);
	}

	@Override
	public synchronized CartAddRes addCart(CustomUserDetails userDetails, CartAddReq cartAddReq) {
		UserInfo user = getUserInfo(userDetails);
		Store store = getStore(cartAddReq.getStoreId());
		Foodie foodie = getFoody(cartAddReq.getFoodieId());

		verifyFoodieInStore(store, foodie);
		verifyCartAddReq(foodie, cartAddReq);

		Cart cart = cartRepository.findByUserInfoAndIsDeletedFalseAndIsOrderedFalse(user)
			.orElseGet(() -> makeCart(user, store, cartAddReq.getInout()));

		verifyItemsInCart(cart, store, cartAddReq.getInout());
		verifyCart(cart, cartAddReq.getInout());
		CartItem cartItem = makeCartItem(cart, foodie, cartAddReq.getCount());
		List<CartOption> cartOptions = cartAddReq.getOptions().stream()
			.map(option -> makeCartOption(cartItem, option))
			.toList();

		cartRepository.save(cart);
		cartItemRepository.save(cartItem);
		cartOptionRepository.saveAll(cartOptions);

		return orderMapper.cartToCartAddRes(cartItem);
	}

	private void verifyItemsInCart(Cart cart, Store store, Long inout) {
		if (getCartItemCount(cart) == EMPTY_CART) {
			changeCartStore(cart, store, inout);
		}
		if (!cart.getStore().equals(store)) {
			throw new BusinessLogicException(ExceptionCode.ITEM_NOT_SAME_STORE);
		}
	}

	private void changeCartStore(Cart cart, Store store, Long inout) {
		cart.setStore(store);
		cart.setInOut(inout);
		cart.getCartItems().forEach(cartItem -> cartItem.setIsDeleted(true));
	}

	@Override
	public CartEidtRes editCart(CustomUserDetails userDetails, Long idx, Long count) {
		CartItem cartItem = getCartItem(idx);
		verifyCart(cartItem.getCart(), null);
		verifyCartItem(cartItem, userDetails);

		editCartItem(cartItem, count);
		cartItemRepository.save(cartItem);
		return orderMapper.cartToCartEditRes(cartItem);
	}

	private void verifyCart(Cart cart, Long inout) {
		if (cart.getIsOrdered()) {
			throw new BusinessLogicException(ExceptionCode.CART_NOT_EDITABLE);
		}
		if (cart.getIsDeleted()) {
			throw new BusinessLogicException(ExceptionCode.CART_NOT_EDITABLE);
		}
		if (inout == null) {
			return;
		}
		if (getCartItemCount(cart) == EMPTY_CART) {
			return;
		}
		if (!cart.getInOut().equals(inout)) {
			throw new BusinessLogicException(ExceptionCode.CART_INOUT_NOT_MATCH);
		}
	}

	private int getCartItemCount(Cart cart) {
		return cart.getCartItems().stream()
			.filter(cartItem -> !cartItem.getIsDeleted())
			.toList()
			.size();
	}

	@Override
	public CartItemDeleteRes deleteCart(CustomUserDetails userDetails, Long idx) {
		CartItem cartItem = getCartItem(idx);

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

	@Override
	public CartGetRes getCart(CustomUserDetails userDetails, Long cartId) {
		UserInfo user = getUserInfo(userDetails);
		Cart cart = getCartId(user, cartId);
		verifyMyCart(user, cart);

		return orderMapper.cartToCartGetRes(cart);
	}

	private void verifyMyCart(UserInfo user, Cart cart) {
		if (cart.getUserInfo().equals(user)) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.NOT_MY_CART);
	}

	private Cart getCartId(UserInfo user, Long cartId) {
		return (cartId == null) ? getCart(user)
			: cartRepository.findById(cartId).orElseGet(() -> getCart(user));
	}

	@Override
	public TosspaymentMakeRes requestTossPayment(CustomUserDetails userDetails, PaymentReq paymentReq) {
		UserInfo user = getUserInfo(userDetails);
		Cart cart = getCartId(user, paymentReq.getCartId());
		Store store = cart.getStore();
		Coupon coupon = getCoupon(paymentReq.getCouponId());
		Long point = paymentReq.getPoint() != null ? paymentReq.getPoint() : 0L;

		verifyStoreOpen(store);
		verifyCoupon(user, coupon);
		verifyCartSoldOut(cart);
		verifyPoint(user, point);

		point *= -1; // 포인트 사용은 음수로 처리

		// Long amount = calculateAmount(store, paymentReq.getCarts(), paymentReq.getInout());
		Long amount = calculateAmount2(cart);
		Order order = makeOrder(user, store, amount, cart, coupon, point);
		cartOrder(cart);
		ordersRepository.save(order);
		cartRepository.save(cart);
		return orderMapper.orderToTosspaymentMakeRes(order);
	}

	private void verifyPoint(UserInfo user, Long point) {
		if (user.getPoint() >= point) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.POINT_NOT_ENOUGH);
	}

	private void verifyCartSoldOut(Cart cart) {
		if (cart.getCartItems()
			.stream()
			.filter(cartItem -> !cartItem.getIsDeleted())
			.anyMatch(cartItem -> cartItem.getFoodie().isSoldOut())) {
			log.error("verifyCartSoldOut CART_SOLD_OUT");
			log.error("userId = " + cart.getUserInfo().getId());
			for (CartItem cartItem : cart.getCartItems()) {
				log.error("cartItem.getFoodie().getId()" + cartItem.getFoodie().getId());
				log.error("cartItem.getFoodie().isSoldOut() = " + cartItem.getFoodie().isSoldOut());
			}
			throw new BusinessLogicException(ExceptionCode.CART_SOLD_OUT);
		}
	}

	private void verifyCoupon(UserInfo user, Coupon coupon) {
		isCoupon(coupon);
		isUserCoupon(user, coupon);
	}

	private void isUserCoupon(UserInfo user, Coupon coupon) {
		if (coupon == null) {
			return;
		}
		if (coupon.getUserInfo().equals(user)) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.COUPON_NOT_ACTIVE);
	}

	private void isCoupon(Coupon coupon) {
		if (coupon == null) {
			return;
		}
		if (coupon.getIssueCount() - coupon.getUseCount() > 0) {
			return;
		}
		if (coupon.getCouponDetail().getExpire().isAfter(LocalDateTime.now())) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.COUPON_NOT_VALID);
	}

	private Coupon getCoupon(Long couponId) {
		if (couponId == null) {
			return null;
		}
		return couponRepository.findById(couponId).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.COUPON_NOT_FOUND)
		);
	}

	private void cartOrder(Cart cart) {
		cart.setIsOrdered(true);
	}

	@Override
	@Transactional
	public PaySuccess tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
		Order order = getOrder(orderId);
		UserInfo user = userServiceFacade.getUserInfoWithPessimisticLock(order.getUserInfo().getId());
		verifyOrder(order, amount);
		verifyUsePoint(order, user);

		TosspaymentDto tosspaymentDto;
		if (amount > 0) {
			tosspaymentDto = requestTossPaymentAccept(paymentKey, orderId, amount);
		} else { // 쿠폰 및 포인트 결제로 0원 결제 시
			tosspaymentDto = makeZeroPaymentDto(paymentKey);
		}

		applyTosspaymentDto(order, tosspaymentDto);
		ordersRepository.save(order);
		if (!Objects.equals(order.getMessage(), TOSSPAYMENT_SUCCESS_MESSAGE)) {
			return orderMapper.tosspaymentDtoToPaySuccess(order.getMessage());
		}
		//TODO: 영수증 처리
		Receipt receipt = orderMapper.tosspaymentDtoToReceipt(tosspaymentDto, order);
		receiptRepository.save(receipt);
		// 포인트 처리
		if (order.getPoint() < 0L) {
			Point point = orderMapper.orderToPoint(order);
			pointRepository.save(point);
			userServiceFacade.saveUserPoint(user, user.getPoint() + order.getPoint());
		}
		return orderMapper.tosspaymentDtoToPaySuccess(TOSSPAYMENT_SUCCESS_MESSAGE);
	}

	private void verifyUsePoint(Order order, UserInfo user) {
		// 포인트 사용 X
		if (order.getPoint() >= 0L) {
			return;
		}
		// 유저 포인트 충분
		if (user.getPoint() + order.getPoint() >= 0L) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.POINT_NOT_ENOUGH);
	}

	private TosspaymentDto makeZeroPaymentDto(String paymentKey) {
		return TosspaymentDto.builder()
			.paymentKey(paymentKey)
			.method(MEMBERSHIP_PAYMENT_METHOD)
			.build();
	}

	@Override
	public FailDto tossPaymentFail(String code, String orderId, String message) {
		Order order = getOrder(orderId);
		applyOrderFail(order);
		ordersRepository.save(order);
		return orderMapper.makeFailDto(code, message);
	}

	@Override
	public HistoryRes getHistories(CustomUserDetails userDetails) {
		UserInfo user = getUserInfo(userDetails);
		List<Order> orders = getOrders(user);
		return orderMapper.ordersToHistoryRes(orders);
	}

	@Override
	@Transactional
	public CurrentRes getCurrent(String orderId) {
		Order order = getOrder(orderId);
		verifyOrderCurrent(order);
		return orderMapper.orderToCurrentRes(order);
	}

	@Override
	@Transactional
	public Object cancelTossPayment(CustomUserDetails userDetails, TossCancelReq tossCancelReq) {
		UserInfo user = userServiceFacade.getUserInfoWithPessimisticLock(userDetails.getId());
		Order order = getOrder(tossCancelReq.getOrderId());

		if (order.getPoint() < 0L) {
			Point point = pointServiceFacade.getPointByOrder(order);
			pointServiceFacade.cancelPoint(point);
		}

		verifyCancel(order, user);
		TosspaymentDto tosspaymentDto = null;
		if (order.getAmount() > 0L) {
			tosspaymentDto = requestTossPaymentCancel(order.getPaymentKey());
		} else {
			tosspaymentDto = makeZeroPaymentCancelDto();
		}

		applyCancelTosspaymentDto(order, tosspaymentDto);
		userServiceFacade.saveUserPoint(user, user.getPoint() - order.getPoint());

		ordersRepository.save(order);
		return orderMapper.tosspaymentDtoToCancelRes();
	}

	private TosspaymentDto makeZeroPaymentCancelDto() {
		return TosspaymentDto.builder()
			.cancels(",=단순 변심")
			.status("CANCELED")
			.build();
	}

	@Override
	@Transactional
	public HistoryDetailRes getReceipt(CustomUserDetails userDetails, String orderId) {
		UserInfo user = getUserInfo(userDetails);
		Order order = getOrder(orderId);
		verifyReceipt(order, user);
		return orderMapper.orderToHistoryDetailRes(order);
	}

	@Override
	public CartCountRes getCartCount(CustomUserDetails userDetails) {
		UserInfo user = getUserInfo(userDetails);
		Cart cart = getCart(user);
		return orderMapper.cartToCartCountRes(cart);
	}

	@Override
	public HistoryRes getNewHistories(CustomUserDetails userDetails) {
		UserInfo user = getUserInfo(userDetails);
		List<Order> orders = getOrders(user);
		return orderMapper.ordersToNewHistoryRes(orders);

	}

	@Override
	public HistoryRes getFastHistories(CustomUserDetails userDetails) {
		UserInfo user = getUserInfo(userDetails);
		List<Order> orders = getOrders(user);
		return orderMapper.ordersToFastOrderRes(orders);
	}

	private void verifyReceipt(Order order, UserInfo user) {
		verifyOrderReceipt(order);
		verifyOrederUser(order, user);
	}

	private void verifyOrderReceipt(Order order) {
		if (order.getProgress().equals(Progress.COMPLETE)
			|| order.getProgress().equals(Progress.PICKUP)
			|| order.getProgress().equals(Progress.CANCEL)
			|| order.getProgress().equals(Progress.ORDER)
			|| order.getProgress().equals(Progress.MAKE)
			|| order.getProgress().equals(Progress.FAIL)) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.ORDER_NOT_RECEIPT);
	}

	private void applyCancelTosspaymentDto(Order order, TosspaymentDto tosspaymentDto) {
		order.setProgress(Progress.CANCEL);
		order.setPayStatus(false);
		order.getReceipt().setCancels(tosspaymentDto.getCancels().toString());
		order.getReceipt().setStatus(tosspaymentDto.getStatus());
		if (order.getCoupon() != null) {
			order.getCoupon().setUseCount(order.getCoupon().getUseCount() - 1);
		}

	}

	private TosspaymentDto requestTossPaymentCancel(String paymentKey) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = makeTossHeader();
		JSONObject params = new JSONObject();

		params.put("cancelReason", USER_CANCEL_RESON);

		try {
			return restTemplate.postForObject(TossPaymentConfig.PAYMENT_URL + paymentKey + "/cancel",
				new HttpEntity<>(params, headers),
				TosspaymentDto.class);
		} catch (HttpClientErrorException e) {
			/*
			 * 취소 실패 시, 이미 취소된 거래라면 결제 정보 조회
			 * 취소된 정보 재적용
			 */
			if (e.getResponseBodyAs(FailDto.class).getCode()
				.equals(TOSS_RESPONSE_FAIL_CANCELED)) {
				return restTemplate.exchange(
						TossPaymentConfig.PAYMENT_URL + paymentKey,
						HttpMethod.GET,
						new HttpEntity<>(headers),
						TosspaymentDto.class)
					.getBody();
			}
			throw new BusinessLogicException(ExceptionCode.TOSS_PAYMENT_SUCCESS_FAIL);

		} catch (Exception e) {
			log.error("e.getMessage() = " + e.getMessage());
			throw new BusinessLogicException(ExceptionCode.TOSS_PAYMENT_SUCCESS_FAIL);
		}
	}

	private void verifyCancel(Order order, UserInfo user) {
		verifyCancelStatus(order);
		verifyOrederUser(order, user);
	}

	private void verifyOrederUser(Order order, UserInfo user) {
		if (order.getUserInfo().equals(user)) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.ORDER_NOT_CANCELABLE);
	}

	private void verifyCancelStatus(Order order) {
		if (order.getProgress().equals(Progress.ORDER)) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.ORDER_NOT_CANCELABLE);
	}

	private void verifyOrderCurrent(Order order) {
		if (order.getProgress().equals(Progress.ORDER)
			|| order.getProgress().equals(Progress.MAKE)
			|| order.getProgress().equals(Progress.COMPLETE)
			|| order.getProgress().equals(Progress.PICKUP)
			|| order.getProgress().equals(Progress.CANCEL)) {
			return;
		}
		throw new BusinessLogicException(ExceptionCode.ORDER_NOT_CURRENT);
	}

	private List<Order> getOrders(UserInfo user) {
		return ordersRepository.findAllByUserInfo(user).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
	}

	private void applyOrderFail(Order order) {
		order.setPayStatus(false);
		order.setProgress(Progress.FAIL);
	}

	private void applyTosspaymentDto(Order order, TosspaymentDto tosspaymentDto) {
		if (tosspaymentDto.getPaymentKey() == null) {
			order.setMessage(tosspaymentDto.getMessage());
			order.setProgress(Progress.FAIL);
			order.setPayStatus(false);
			return;
		}
		order.setOrderNumber(getOrderNumber(order));
		order.setPaymentKey(tosspaymentDto.getPaymentKey());
		order.setMethod(tosspaymentDto.getMethod());
		order.setProgress(Progress.ORDER);
		order.setPayStatus(true);
		order.getCart().setIsOrdered(true);
		order.setMessage(TOSSPAYMENT_SUCCESS_MESSAGE);
		if (order.getCoupon() != null) {
			order.getCoupon().setUseCount(order.getCoupon().getUseCount() + 1);
		}
	}

	private String getOrderNumber(Order order) {
		OrderNumberSequence sequence = orderNumberSequenceRepository.findOrderNumberByStoreId(order.getStore().getId())
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NUMBER_NOT_FOUND));
		sequence.increaseOrderNumber();
		orderNumberSequenceRepository.save(sequence);
		return Long.toString(sequence.getOrderNumber());
	}

	private void verifyOrder(Order order, Long amount) {
		if (order.getPayStatus() != null) {
			throw new BusinessLogicException(ExceptionCode.ORDER_ALREADY_END);
		}
		if (!order.getAmount().equals(amount)) {
			throw new BusinessLogicException(ExceptionCode.TOSS_PAYMENT_AMOUNT_NOT_MATCH);
		}
	}

	private Order getOrder(String orderId) {
		return ordersRepository.findByOrderId(orderId).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND)
		);
	}

	private TosspaymentDto requestTossPaymentAccept(String paymentKey, String orderId, Long amount) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = makeTossHeader();
		JSONObject params = new JSONObject();

		params.put("amount", amount);
		params.put("orderId", orderId);
		params.put("paymentKey", paymentKey);

		try {
			return restTemplate.postForObject(TossPaymentConfig.CONFIRM_URL,
				new HttpEntity<>(params, headers),
				TosspaymentDto.class);
		} catch (HttpClientErrorException e) {
			return e.getResponseBodyAs(TosspaymentDto.class);
		} catch (Exception e) {
			log.error("e.getMessage() = " + e.getMessage());
			throw new BusinessLogicException(ExceptionCode.TOSS_PAYMENT_SUCCESS_FAIL);
		}
	}

	private HttpHeaders makeTossHeader() {
		HttpHeaders headers = new HttpHeaders();
		String encodedAuthKey = new String(
			Base64.getEncoder().encode((tosspaymentConfig.getTossSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuthKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private Order makeOrder(UserInfo user, Store store, Long amount, Cart cart, Coupon coupon, Long point) {
		List<CartItem> cartItems = cart.getCartItems().stream()
			.filter(cartItem -> !cartItem.getIsDeleted())
			.toList();

		if (cartItems.stream().allMatch(CartItem::getIsDeleted)) {
			throw new BusinessLogicException(ExceptionCode.CART_NOT_FOUND);
		}
		CartItem firstItem = cartItems.get(0);
		String orderName =
			firstItem.getCount() == 1 ? firstItem.getFoodie().getName() :
				firstItem.getFoodie().getName() + " * " + firstItem.getCount();
		if (cartItems.size() > 1) {
			orderName += " 외 "
				+ (cartItems.stream().filter(cartItem -> !cartItem.getIsDeleted()).count() - 1) + "개";
		}

		Long couponAmount = Math.max(0, amount - (coupon != null ? coupon.getCouponDetail().getSalePrice() : 0));
		Long pointAmount = Math.max(0, couponAmount + point);

		return Order.builder()
			.userInfo(user)
			.store(store)
			.amount(pointAmount)
			.orderId(UUID.randomUUID().toString())
			.cart(cart)
			.coupon(coupon)
			.paymentKey(null)
			.point(pointAmount - couponAmount)
			.orderName(orderName)
			.totalAmount(amount)
			.orderNumber(null)
			.progress(Progress.REQUEST)
			.inOut(cart.getInOut())
			.build();
	}

	// private Long calculateAmount(Store store, List<FoodyDto> carts, Long inout) {
	// 	return carts.stream()
	// 		.map(FoodyDto -> {
	// 			Foodie foodie = getFoody(FoodyDto.getIdx());
	// 			verifyFoodieInStore(store, foodie);
	// 			verifyOption(foodie, FoodyDto.getOptions());
	// 			verifyEssentialOption(foodie, FoodyDto.getOptions());
	//
	// 			Long price = orderMapper.determinePrice(foodie, inout);
	// 			Long totalPrice = FoodyDto.getOptions().stream()
	// 				.map(this::getFoodieOption)
	// 				.map(FoodieOption::getPrice)
	// 				.reduce(price, Long::sum);
	//
	// 			return totalPrice * FoodyDto.getCount();
	// 		})
	// 		.reduce(0L, Long::sum);
	// }

	private Long calculateAmount2(Cart cart) {
		return cart.getCartItems().stream()
			.filter(cartItem -> !cartItem.getIsDeleted())
			.map(cartItem -> {
				Long price = orderMapper.determinePrice(cartItem.getFoodie(), cart.getInOut());
				Long totalPrice = cartItem.getCartOptions().stream()
					.map(CartOption::getFoodieOption)
					.map(FoodieOption::getPrice)
					.reduce(price, Long::sum);

				return totalPrice * cartItem.getCount();
			}).reduce(0L, Long::sum);
	}

	private void resetCartItem(Cart cart) {
		cart.setIsDeleted(true);
	}

	private Cart getCart(UserInfo user) {
		return cartRepository.findByUserInfoAndIsDeletedFalseAndIsOrderedFalse(user).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND)
		);
	}

	private void deleteCartItem(CartItem cartItem) {
		cartItem.setIsDeleted(true);
	}

	private void editCartItem(CartItem cartItem, Long count) {
		cartItem.setCount(count);
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

	private Cart makeCart(UserInfo user, Store store, Long inout) {
		return Cart.builder()
			.userInfo(user)
			.store(store)
			.inOut(inout)
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
		verifyStoreOpen(foodie.getFoodieCategory().getStore());
		verifyFoodyNotSoldOut(foodie);
		verifyOption(foodie, cartAddReq.getOptions());
		verifyEssentialOption(foodie, cartAddReq.getOptions());
		verifyInout(cartAddReq.getInout());
	}

	private void verifyFoodyNotSoldOut(Foodie foodie) {
		if (foodie.isSoldOut()) {
			throw new BusinessLogicException(ExceptionCode.FOODY_NOT_FOUND);
		}
	}

	private void verifyStoreOpen(Store store) {
		if (!store.isStatus()) {
			throw new BusinessLogicException(ExceptionCode.STORE_NOT_OPEN);
		}
	}

	private void verifyInout(Long inout) {
		if (!inout.equals(EAT_IN) && !inout.equals(TAKE_OUT)) {
			throw new BusinessLogicException(ExceptionCode.INVALID_INOUT);
		}
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
