package com.readyvery.readyverydemo.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.readyvery.readyverydemo.domain.OrderNumberSequence;

import jakarta.persistence.LockModeType;

public interface OrderNumberSequenceRepository extends JpaRepository<OrderNumberSequence, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<OrderNumberSequence> findOrderNumberByStoreId(Long storeId);
}
