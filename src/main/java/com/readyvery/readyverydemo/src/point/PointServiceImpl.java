package com.readyvery.readyverydemo.src.point;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.point.dto.GetPointRes;
import com.readyvery.readyverydemo.src.point.dto.PointMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final UserRepository userRepository;
	private final PointMapper pointMapper;

	@Override
	public GetPointRes getPoint(CustomUserDetails userDetails) {
		UserInfo userInfo = userRepository.getReferenceById(userDetails.getId());
		return pointMapper.toGetPointRes(userInfo.getPoint());
	}
}
