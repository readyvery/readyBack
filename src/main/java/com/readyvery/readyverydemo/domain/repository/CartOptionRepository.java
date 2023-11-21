package com.readyvery.readyverydemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.CartOption;

public interface CartOptionRepository extends JpaRepository<CartOption, Long> {
}
