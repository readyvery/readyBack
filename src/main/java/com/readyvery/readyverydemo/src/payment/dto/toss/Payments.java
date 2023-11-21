package com.readyvery.readyverydemo.src.payment.dto.toss;

import java.time.LocalDateTime;

import com.readyvery.readyverydemo.src.payment.dto.TossPaymentSuccessRes;

public class Payments {
	private String version;
	private String paymentKey;
	private String type;
	private String orderId;
	private String orderName;
	private String mid;
	private String currency;
	private String method;
	private Long totalAmount;
	private Long balanceAmount;
	private String status;
	private LocalDateTime requestedAt;
	private LocalDateTime approvedAt;
	private Boolean useEscrow;
	private String lastTransactionKey; // nullable
	private Long suppliedAmount;
	private Long vat;
	private Boolean cultureExpense;
	private Long taxFreeAmount;
	private Long taxExemptedAmount;
	private Cancels cancels;
	private Boolean isPartialCancel;
	private Card card;
	private VirtualAccount virtualAccount;
	private String secret;
	private MobliePhone mobilePhone;
	private GiftCertificate giftCertificate;
	private Transfer transfer;
	private Receipt receipt;
	private Checkout checkout;
	private EasyPay easyPay;
	private String country;
	private Failure failure;
	private CashReceipt cashReceipt;
	private CashReceipts cashReceipts;
	private Discount discount;

	public TossPaymentSuccessRes toTossPaymentSuccessRes() {
		return TossPaymentSuccessRes.builder()
			.orderId(orderId)
			.orderName(orderName)
			.method(method)
			.totalAmount(totalAmount)
			.status(status)
			.requestedAt(requestedAt)
			.approvedAt(approvedAt)
			.build();
	}
}
