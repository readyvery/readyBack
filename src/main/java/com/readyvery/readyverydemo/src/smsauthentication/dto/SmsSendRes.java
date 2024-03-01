package com.readyvery.readyverydemo.src.smsauthentication.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SmsSendRes {

	private boolean isSuccess;
	private String smsMessage;
}
