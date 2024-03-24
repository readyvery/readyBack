package com.readyvery.readyverydemo.src.point;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Point;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceFacade {
	private final PointRepository pointRepository;

	public List<Point> findAllByUserInfo(UserInfo userInfo) {
		return pointRepository.findAllByUserInfo(userInfo);
	}
}
