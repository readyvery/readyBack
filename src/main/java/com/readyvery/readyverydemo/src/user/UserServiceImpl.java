package com.readyvery.readyverydemo.src.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;
import com.readyvery.readyverydemo.src.user.dto.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public UserAuthRes getUserAuthByEmail(String email) {
		UserInfo userInfo = getUserInfo(email);
		return userMapper.userInfoToUserAuthRes(userInfo);

	}

	@Override
	public UserInfoRes getUserInfoByEmail(String email) {
		UserInfo userInfo = getUserInfo(email);
		return userMapper.userInfoToUserInfoRes(userInfo);
	}

	private UserInfo getUserInfo(String email) {
		return userRepository.findByEmail(email).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}
}



