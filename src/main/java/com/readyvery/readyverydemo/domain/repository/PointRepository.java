package com.readyvery.readyverydemo.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Point;
import com.readyvery.readyverydemo.domain.UserInfo;

public interface PointRepository extends JpaRepository<Point, Long> {
	List<Point> findAllByUserInfo(UserInfo userInfo);
}
