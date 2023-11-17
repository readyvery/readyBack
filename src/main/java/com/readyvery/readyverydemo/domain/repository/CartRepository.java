package com.readyvery.readyverydemo.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Cart;
import com.readyvery.readyverydemo.domain.UserInfo;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserInfoAndIsDeletedFalse(UserInfo userInfo);
}
