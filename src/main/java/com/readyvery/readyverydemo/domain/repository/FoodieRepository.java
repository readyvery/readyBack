package com.readyvery.readyverydemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Foodie;

public interface FoodieRepository extends JpaRepository<Foodie, Long> {
}
