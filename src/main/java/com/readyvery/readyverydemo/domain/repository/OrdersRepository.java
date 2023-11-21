package com.readyvery.readyverydemo.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Order;
import com.readyvery.readyverydemo.domain.UserInfo;

public interface OrdersRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByOrderId(String orderId);

	Optional<List<Order>> findAllByUserInfo(UserInfo userInfo);
}
