package com.readyvery.readyverydemo.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Order;
import com.readyvery.readyverydemo.domain.Point;
import com.readyvery.readyverydemo.domain.UserInfo;

public interface PointRepository extends JpaRepository<Point, Long> {
	List<Point> findAllByUserInfo(UserInfo userInfo);

	Optional<Point> findByOrder(Order order);
}
