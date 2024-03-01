package com.readyvery.readyverydemo.src.smsauthentication;

import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsRegisterUserPhoneReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsRegisterUserPhoneRes;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsSendReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsSendRes;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsVerifyReq;
import com.readyvery.readyverydemo.src.smsauthentication.dto.SmsVerifyRes;

public interface SmsService {

	SmsSendRes sendSms(Long id, SmsSendReq smsSendReq);

	SmsVerifyRes verifySms(Long id, SmsVerifyReq smsVerifyReq);

	SmsRegisterUserPhoneRes authoritySms(Long id, SmsRegisterUserPhoneReq smsRegisterUserPhoneReq);
}
