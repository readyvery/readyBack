package com.readyvery.readyverydemo.src.point;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Order;
import com.readyvery.readyverydemo.domain.Point;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.PointRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceFacade {
	private final PointRepository pointRepository;

	public List<Point> findAllByUserInfo(UserInfo userInfo) {
		return pointRepository.findAllByUserInfo(userInfo);
	}

	public Point getPointByOrder(Order order) {
		return pointRepository.findByOrder(order)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.POINT_NOT_FOUND));
	}

	public void cancelPoint(Point point) {
		point.setIsDeleted(true);
		pointRepository.save(point);
	}
}
