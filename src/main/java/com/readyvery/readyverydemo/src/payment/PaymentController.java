package com.readyvery.readyverydemo.src.payment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	// @PostMapping("/toss") // 토스 결제 요청
	// public ResponseEntity<TossPaymentRes> requestTossPayment(@RequestBody PaymentReq paymentReq) {
	// 	TossPaymentRes tossPaymentRes = paymentService.requestTossPayment(paymentReq);
	// 	return new ResponseEntity<>(tossPaymentRes, HttpStatus.OK);
	// }

	// @GetMapping("/toss/success") // 토스 결제 성공
	// public ResponseEntity<TossPaymentSuccessRes> successTossPayment(
	// 	@RequestParam String paymentKey,
	// 	@RequestParam String orderId,
	// 	@RequestParam Long amount) {
	//
	// 	return new ResponseEntity<>(
	// 		paymentService.tossPaymentSuccess(paymentKey, orderId, amount).toTossPaymentSuccessRes(),
	// 		HttpStatus.OK);
	// }
}
