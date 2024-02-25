package com.readyvery.readyverydemo.src.point;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.PointRepository;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.point.dto.GetPointHistoryRes;
import com.readyvery.readyverydemo.src.point.dto.GetPointRes;
import com.readyvery.readyverydemo.src.point.dto.PointMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final UserRepository userRepository;
	private final PointRepository pointRepository;
	private final PointMapper pointMapper;

	@Override
	public GetPointRes getPoint(CustomUserDetails userDetails) {
		UserInfo userInfo = getUserInfo(userDetails.getId());
		return pointMapper.toGetPointRes(userInfo.getPoint());
	}

	@Override
	public GetPointHistoryRes getPointHistory(CustomUserDetails userDetails) {
		UserInfo userInfo = getUserInfo(userDetails.getId());
		return pointMapper.toGetPointHistoryRes(pointRepository.findAllByUserInfo(userInfo));
	}

	private UserInfo getUserInfo(Long id) {
		return userRepository.findById(id).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}
}
