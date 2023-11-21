package com.readyvery.readyverydemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
