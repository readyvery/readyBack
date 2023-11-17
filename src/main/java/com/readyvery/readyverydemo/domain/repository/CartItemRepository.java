package com.readyvery.readyverydemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
