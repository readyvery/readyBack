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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SmsController {

	private final SmsService smsServiceImpl;

	@Operation(summary = "SMS 인증 번호 발급 기능", description = "SMS 인증 번호를 발급합니다.", tags = {"SMS 인증"})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
	})
	@PostMapping("/sms/send")
	public SmsSendRes sendSms(
		@RequestBody SmsSendReq smsSendReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return smsServiceImpl.sendSms(userDetails.getId(), smsSendReq);
	}

	@Operation(summary = "SMS 인증 번호 검증 기능", description = "SMS 인증 번호를 검증합니다.", tags = {"SMS 인증"})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
	})
	@PostMapping("/sms/verify")
	public SmsVerifyRes verifySms(
		@RequestBody SmsVerifyReq smsVerifyReq, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return smsServiceImpl.verifySms(userDetails.getId(), smsVerifyReq);
	}

	@Operation(summary = "SMS 인증 번호 권한 변경", description = "SMS 인증된 전화번호를 등록합니다.", tags = {"SMS 인증"})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
	})
	@PostMapping("/sms/authority")
	public SmsRegisterUserPhoneRes authoritySms(
		@RequestBody SmsRegisterUserPhoneReq smsRegisterUserPhoneReq,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return smsServiceImpl.authoritySms(userDetails.getId(), smsRegisterUserPhoneReq);
	}

}
