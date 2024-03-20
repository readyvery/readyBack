package com.readyvery.readyverydemo.src.smsauthentication;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.config.SolApiConfig;
import com.readyvery.readyverydemo.domain.Role;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsRegisterUserPhoneReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsRegisterUserPhoneRes;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsSendReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsSendRes;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsVerifyReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsVerifyRes;
import com.readyvery.readyverydemo.src.user.UserServiceFacade;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

	private final SolApiConfig solApiConfig;
	private final MessageSendingService messageSendingService;
	private final VerificationService verificationService;
	private final UserServiceFacade userServiceFacade;

	@Override
	public SmsSendRes sendSms(Long id, SmsSendReq smsSendReq) {
		// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요

		duplicateCheck(id, smsSendReq.getPhoneNumber());
		String code = verificationService.createVerificationCode(smsSendReq.getPhoneNumber(), false);

		String messageContent = "[Readyvery] 아래의 인증번호를 입력해주세요.\n인증번호 : " + code;
		boolean isMessageSent = messageSendingService.sendMessage(smsSendReq.getPhoneNumber(),
			solApiConfig.getPhoneNumber(), messageContent);

		if (isMessageSent) {
			return SmsSendRes.builder()
				.isSuccess(true)
				.smsMessage("인증번호가 발송되었습니다.")
				.build();
		} else {
			log.error("Message sending failed.");
			return SmsSendRes.builder()
				.isSuccess(false)
				.smsMessage("메시지 발송에 실패하였습니다.")
				.build();
		}
	}

	@Override
	public SmsVerifyRes verifySms(Long id, SmsVerifyReq smsVerifyReq) {

		duplicateCheck(id, smsVerifyReq.getPhoneNumber());
		// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
		boolean isValid = verificationService.verifyCode(smsVerifyReq.getPhoneNumber(), smsVerifyReq.getVerifyNumber());
		if (isValid) {

			return SmsVerifyRes.builder()
				.isSuccess(true)
				.smsMessage("인증에 성공하였습니다.")
				.build();
		} else {
			return SmsVerifyRes.builder()
				.isSuccess(false)
				.smsMessage("인증에 실패하였습니다.")
				.build();
		}

	}

	@Override
	public SmsRegisterUserPhoneRes authoritySms(Long id, SmsRegisterUserPhoneReq smsRegisterUserPhoneReq) {

		UserInfo userInfo = duplicateCheck(id, smsRegisterUserPhoneReq.getPhoneNumber());
		if (verificationService.verifyNumber(smsRegisterUserPhoneReq.getPhoneNumber())) {
			userServiceFacade.updateUserPhone(userInfo, smsRegisterUserPhoneReq.getPhoneNumber());
			return SmsRegisterUserPhoneRes.builder()
				.isSuccess(true)
				.smsMessage("전화번호 등록을 완료하였습니다.")
				.build();
		} else {
			return SmsRegisterUserPhoneRes.builder()
				.isSuccess(false)
				.smsMessage("전화번호 등록을 실패하였습니다.")
				.build();
		}
	}

	public UserInfo duplicateCheck(Long id, String phoneNumber) {
		if (StringUtils.isEmpty(phoneNumber)) {
			throw new BusinessLogicException(ExceptionCode.INVALID_INPUT);
		}
		UserInfo userInfo = userServiceFacade.getUserInfo(id);
		if (userInfo.getRole() == null || userInfo.getRole().equals(Role.USER)) {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
		return userInfo;
	}

}
