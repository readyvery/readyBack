package com.readyvery.readyverydemo.src.refreshtoken.fallback;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenFallbackRepository extends JpaRepository<RefreshTokenFallback, String> {
} 