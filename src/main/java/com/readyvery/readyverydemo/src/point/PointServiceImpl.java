package com.readyvery.readyverydemo.src.point;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.src.point.dto.PointMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final UserInfo userInfo;
	private final PointMapper pointMapper;
}
