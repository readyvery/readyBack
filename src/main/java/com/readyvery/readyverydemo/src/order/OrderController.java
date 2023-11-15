package com.readyvery.readyverydemo.src.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
