package com.readyvery.readyverydemo.src.user;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceFacade {

	private final UserRepository userRepository;

	public UserInfo getUserInfo(Long id) {
		return userRepository.findById(id).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}

	public UserInfo getUserInfoByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}
}
