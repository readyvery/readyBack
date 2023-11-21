package com.readyvery.readyverydemo.src.payment.dto.toss;

import java.time.LocalDateTime;

public class Cancels {
	private Long cancelAmount;
	private String cancelReason;
	private Long taxFreeAmount;
	private Long taxExemptionAmount;
	private Long refundableAmount;
	private Long easyPayDiscountAmount;
	private LocalDateTime canceledAt;
	private String transactionKey;
	private String receiptKey;
}
