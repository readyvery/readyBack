package com.readyvery.readyverydemo.src.order.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryRes {
	private List<ReceiptHistoryDto> receipts;
}
