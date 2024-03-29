package com.readyvery.readyverydemo.src.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
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
import com.readyvery.readyverydemo.src.order.dto.PaySuccess;
import com.readyvery.readyverydemo.src.order.dto.PaymentReq;
import com.readyvery.readyverydemo.src.order.dto.TossCancelReq;
import com.readyvery.readyverydemo.src.order.dto.TosspaymentMakeRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@GetMapping("/{storeId}")
	public ResponseEntity<FoodyDetailRes> getFoody(
		@PathVariable("storeId") Long storeId,
		@RequestParam("foody_id") Long foodyId,
		@RequestParam("inout") Long inout) {
		FoodyDetailRes foodyDetailRes = orderService.getFoody(storeId, foodyId, inout);
		return new ResponseEntity<>(foodyDetailRes, HttpStatus.OK);
	}

	@GetMapping("/cart")
	public ResponseEntity<CartGetRes> getCart(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(value = "cartId", required = false) Long cartId) {
		CartGetRes cartGetRes = orderService.getCart(userDetails, cartId);
		return new ResponseEntity<>(cartGetRes, HttpStatus.OK);
	}

	@GetMapping("/cart/count")
	public ResponseEntity<CartCountRes> getCartCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
		CartCountRes cartCount = orderService.getCartCount(userDetails);
		return new ResponseEntity<>(cartCount, HttpStatus.OK);
	}

	@GetMapping("/toss/success")
	public ResponseEntity<PaySuccess> tossPaymentSuccess(
		@RequestParam("paymentKey") String paymentKey,
		@RequestParam("orderId") String orderId,
		@RequestParam("amount") Long amount) {
		PaySuccess result = orderService.tossPaymentSuccess(paymentKey, orderId, amount);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/toss/fail")
	public ResponseEntity<FailDto> tossPaymentFail(
		@RequestParam("code") String code,
		@RequestParam("orderId") String orderId,
		@RequestParam("message") String message) {
		FailDto result = orderService.tossPaymentFail(code, orderId, message);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/history/fast")
	public ResponseEntity<HistoryRes> getFastHistories(@AuthenticationPrincipal CustomUserDetails userDetails) {
		HistoryRes historyRes = orderService.getFastHistories(userDetails);
		return new ResponseEntity<>(historyRes, HttpStatus.OK);
	}

	@GetMapping("/history/old")
	public ResponseEntity<HistoryRes> getHistories(@AuthenticationPrincipal CustomUserDetails userDetails) {
		HistoryRes historyRes = orderService.getHistories(userDetails);
		return new ResponseEntity<>(historyRes, HttpStatus.OK);
	}

	@GetMapping("/history/new")
	public ResponseEntity<HistoryRes> getNewHistories(@AuthenticationPrincipal CustomUserDetails userDetails) {
		HistoryRes historyRes = orderService.getNewHistories(userDetails);
		return new ResponseEntity<>(historyRes, HttpStatus.OK);
	}

	@GetMapping("/receipt")
	public ResponseEntity<HistoryDetailRes> getReceipts(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam("orderId") String orderId) {
		HistoryDetailRes historyRes = orderService.getReceipt(userDetails, orderId);
		return new ResponseEntity<>(historyRes, HttpStatus.OK);
	}

	@GetMapping("/current")
	public ResponseEntity<CurrentRes> getCurrent(@RequestParam("orderId") String orderId) {
		CurrentRes currentRes = orderService.getCurrent(orderId);
		return new ResponseEntity<>(currentRes, HttpStatus.OK);
	}

	@PostMapping("/cart")
	public ResponseEntity<CartAddRes> addCart(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody CartAddReq cartAddReq) {
		CartAddRes cartAddRes = orderService.addCart(userDetails, cartAddReq);
		return new ResponseEntity<>(cartAddRes, HttpStatus.OK);
	}

	@PostMapping("/toss")
	public ResponseEntity<TosspaymentMakeRes> requestTossPayment(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody PaymentReq paymentReq) {
		TosspaymentMakeRes tosspaymentMakeRes = orderService.requestTossPayment(userDetails, paymentReq);
		return new ResponseEntity<>(tosspaymentMakeRes, HttpStatus.OK);
	}

	@PostMapping("/toss/cancel")
	public ResponseEntity<Object> cancelTossPayment(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody TossCancelReq tossCancelReq) {
		Object result = orderService.cancelTossPayment(userDetails, tossCancelReq);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/cart")
	public ResponseEntity<CartEidtRes> updateCart(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam Long idx,
		@RequestParam Long count) {
		CartEidtRes cartEditRes = orderService.editCart(userDetails, idx, count);
		return new ResponseEntity<>(cartEditRes, HttpStatus.OK);
	}

	@DeleteMapping("/cart")
	public ResponseEntity<CartItemDeleteRes> deleteCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam Long idx) {
		CartItemDeleteRes cartItemDeleteRes = orderService.deleteCart(userDetails, idx);
		return new ResponseEntity<>(cartItemDeleteRes, HttpStatus.OK);
	}

	@DeleteMapping("/cart/reset")
	public ResponseEntity<CartResetRes> resetCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
		CartResetRes cartResetRes = orderService.resetCart(userDetails);
		return new ResponseEntity<>(cartResetRes, HttpStatus.OK);
	}
}
