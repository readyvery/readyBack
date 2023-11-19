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
import com.readyvery.readyverydemo.src.order.dto.CartEditReq;
import com.readyvery.readyverydemo.src.order.dto.CartEidtRes;
import com.readyvery.readyverydemo.src.order.dto.CartGetRes;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteReq;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteRes;
import com.readyvery.readyverydemo.src.order.dto.CartResetRes;
import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;

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
		@RequestParam("inout") Long inout) {
		CartGetRes cartGetRes = orderService.getCart(userDetails, inout);
		return new ResponseEntity<>(cartGetRes, HttpStatus.OK);
	}

	@PostMapping("/cart")
	public ResponseEntity<CartAddRes> addCart(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody CartAddReq cartAddReq) {
		CartAddRes cartAddRes = orderService.addCart(userDetails, cartAddReq);
		return new ResponseEntity<>(cartAddRes, HttpStatus.OK);
	}

	@PutMapping("/cart")
	public ResponseEntity<CartEidtRes> updateCart(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody CartEditReq cartEditReq) {
		CartEidtRes cartEditRes = orderService.editCart(userDetails, cartEditReq);
		return new ResponseEntity<>(cartEditRes, HttpStatus.OK);
	}

	@DeleteMapping("/cart")
	public ResponseEntity<CartItemDeleteRes> deleteCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody CartItemDeleteReq cartItemDeleteReq) {
		CartItemDeleteRes cartItemDeleteRes = orderService.deleteCart(userDetails, cartItemDeleteReq);
		return new ResponseEntity<>(cartItemDeleteRes, HttpStatus.OK);
	}

	@DeleteMapping("/cart/reset")
	public ResponseEntity<CartResetRes> resetCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
		CartResetRes cartResetRes = orderService.resetCart(userDetails);
		return new ResponseEntity<>(cartResetRes, HttpStatus.OK);
	}
}
