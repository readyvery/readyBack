package com.readyvery.readyverydemo.src.order;

import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.order.dto.CartAddReq;
import com.readyvery.readyverydemo.src.order.dto.CartAddRes;
import com.readyvery.readyverydemo.src.order.dto.CartEditReq;
import com.readyvery.readyverydemo.src.order.dto.CartEidtRes;
import com.readyvery.readyverydemo.src.order.dto.CartGetRes;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteReq;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteRes;
import com.readyvery.readyverydemo.src.order.dto.CartResetRes;
import com.readyvery.readyverydemo.src.order.dto.FailDto;
import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;
import com.readyvery.readyverydemo.src.order.dto.HistoryRes;
import com.readyvery.readyverydemo.src.order.dto.PaymentReq;
import com.readyvery.readyverydemo.src.order.dto.TosspaymentMakeRes;

public interface OrderService {
	FoodyDetailRes getFoody(Long storeId, Long foodyId, Long inout);

	CartAddRes addCart(CustomUserDetails userDetails, CartAddReq cartAddReq);

	CartEidtRes editCart(CustomUserDetails userDetails, CartEditReq cartEditReq);

	CartItemDeleteRes deleteCart(CustomUserDetails userDetails, CartItemDeleteReq cartItemDeleteReq);

	CartResetRes resetCart(CustomUserDetails userDetails);

	CartGetRes getCart(CustomUserDetails userDetails, Long inout);

	TosspaymentMakeRes requestTossPayment(CustomUserDetails userDetails, PaymentReq paymentReq);

	String tossPaymentSuccess(String paymentKey, String orderId, Long amount);

	FailDto tossPaymentFail(String code, String orderId, String message) throws BusinessLogicException;

	HistoryRes getHistories(CustomUserDetails userDetails);
}
