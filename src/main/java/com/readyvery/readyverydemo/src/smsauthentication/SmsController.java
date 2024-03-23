package com.readyvery.readyverydemo.src.smsauthentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsRegisterUserPhoneReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsRegisterUserPhoneRes;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsSendReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsSendRes;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsVerifyReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsVerifyRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SmsController {

	private final SmsService smsServiceImpl;

	@PostMapping("/sms/send")
	public SmsSendRes sendSms(
		@RequestBody SmsSendReq smsSendReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return smsServiceImpl.sendSms(userDetails.getId(), smsSendReq);
	}

	@PostMapping("/sms/verify")
	public SmsVerifyRes verifySms(
		@RequestBody SmsVerifyReq smsVerifyReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return smsServiceImpl.verifySms(userDetails.getId(), smsVerifyReq);
	}

	@PostMapping("/sms/authority")
	public SmsRegisterUserPhoneRes authoritySms(
		@RequestBody SmsRegisterUserPhoneReq smsRegisterUserPhoneReq,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return smsServiceImpl.authoritySms(userDetails.getId(), smsRegisterUserPhoneReq);
	}

}
