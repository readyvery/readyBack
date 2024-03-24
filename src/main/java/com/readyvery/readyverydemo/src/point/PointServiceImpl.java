package com.readyvery.readyverydemo.src.point;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.point.dto.GetPointHistoryRes;
import com.readyvery.readyverydemo.src.point.dto.GetPointRes;
import com.readyvery.readyverydemo.src.point.dto.PointMapper;
import com.readyvery.readyverydemo.src.user.UserServiceFacade;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final UserServiceFacade userServiceFacade;
	private final PointServiceFacade pointServiceFacade;
	private final PointMapper pointMapper;

	@Override
	public GetPointRes getPoint(CustomUserDetails userDetails) {
		UserInfo userInfo = userServiceFacade.getUserInfo(userDetails.getId());
		return pointMapper.toGetPointRes(userInfo.getPoint());
	}

	@Override
	public GetPointHistoryRes getPointHistory(CustomUserDetails userDetails) {
		UserInfo userInfo = userServiceFacade.getUserInfo(userDetails.getId());
		return pointMapper.toGetPointHistoryRes(pointServiceFacade.findAllByUserInfo(userInfo));
	}
}
