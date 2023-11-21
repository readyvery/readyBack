package com.readyvery.readyverydemo.src.payment;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.order.dto.PaymentReq;
import com.readyvery.readyverydemo.src.payment.dto.TossPaymentRes;

public interface PaymentService {

	TossPaymentRes requestTossPayment(CustomUserDetails userDetails, PaymentReq paymentReq);
}
