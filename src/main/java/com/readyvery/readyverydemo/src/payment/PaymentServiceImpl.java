package com.readyvery.readyverydemo.src.payment;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.order.config.TossPaymentConfig;
import com.readyvery.readyverydemo.src.order.dto.PaymentReq;
import com.readyvery.readyverydemo.src.payment.dto.TossPaymentRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final TossPaymentConfig tosspaymentConfig;

	@Override
	public TossPaymentRes requestTossPayment(CustomUserDetails userDetails, PaymentReq paymentReq) {
		return null;
	}

	// @Override
	// public TossPaymentRes requestTossPayment(PaymentReq paymentReq) {
	// 	Long amount = calculateTotalAmount();
	//
	// 	return null;
	// }
	//
	// private Long calculateTotalAmount() {
	// 	//TODO: 장바구니, 장바구니 상세에서 총 금액 계산
	// 	return 0L;
	// }
	//
	// @Override
	// public Payments tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
	// 	// TODO: 검증, 결제 완료 처리
	// 	Payments result = requestTossPaymentAccept(paymentKey, orderId, amount);
	//
	// 	return null;
	// }
	//
	// private Payments requestTossPaymentAccept(String paymentKey, String orderId, Long amount) {
	// 	RestTemplate restTemplate = new RestTemplate();
	// 	HttpHeaders headers = makeTossHeader();
	// 	JSONObject params = new JSONObject();
	//
	// 	params.put("amount", amount);
	// 	params.put("orderId", orderId);
	// 	params.put("paymentKey", paymentKey);
	//
	// 	Payments result = null;
	// 	try {
	// 		result = restTemplate.postForObject(TossPaymentConfig.CONFIRM_URL,
	// 			new HttpEntity<>(params, headers),
	// 			Payments.class);
	// 	} catch (Exception e) {
	// 		throw new RuntimeException(); // 추후에 오류 수정 (이미 승인 됨)
	// 	}
	//
	// 	return result;
	// }
	//
	// private HttpHeaders makeTossHeader() {
	// 	HttpHeaders headers = new HttpHeaders();
	// 	String encodedAuthKey = new String(
	// 		Base64.getEncoder().encode((tosspaymentConfig.getTossSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
	// 	headers.setBasicAuth(encodedAuthKey);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);
	// 	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	// 	return headers;
	// }
}
