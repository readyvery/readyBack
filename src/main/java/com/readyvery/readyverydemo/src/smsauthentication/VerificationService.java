package com.readyvery.readyverydemo.src.smsauthentication;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationService {

	private final RedisTemplate<String, String> redisTemplate;

	public String createVerificationCode(String phoneNumber, boolean someBooleanValue) {
		//String code = UUID.randomUUID().toString().substring(0, 6);
		int sixDigitNumber = (int)(Math.random() * 900000) + 100000; // 100000 ~ 999999 사이의 숫자
		String code = Integer.toString(sixDigitNumber); // 숫자를 문자열로 변환

		redisTemplate.opsForValue().set(phoneNumber + ":code", code, 3, TimeUnit.MINUTES);
		redisTemplate.opsForValue().set(phoneNumber + ":flag", String.valueOf(someBooleanValue), 3, TimeUnit.MINUTES);
		return code;
	}

	public boolean verifyCode(String phoneNumber, String code) {
		String storedCodeKey = phoneNumber + ":code";
		String flagKey = phoneNumber + ":flag";

		String storedCode = redisTemplate.opsForValue().get(storedCodeKey);
		if (storedCode != null && storedCode.equals(code)) {
			// 인증 코드가 일치하면 플래그 값을 true로 설정
			redisTemplate.opsForValue().set(flagKey, "true", 3, TimeUnit.MINUTES);
			return true;
		} else {
			return false;
		}
	}

	public boolean verifyNumber(String phoneNumber) {
		String storedFlag = redisTemplate.opsForValue().get(phoneNumber + ":flag");
		return Boolean.parseBoolean(storedFlag);
	}
}
