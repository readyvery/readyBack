package com.readyvery.readyverydemo.src.refreshtoken.fallback;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenFallbackRepository extends JpaRepository<RefreshTokenFallback, String> {

	@Query("SELECT r.userId FROM RefreshTokenFallback r " +
		   "WHERE r.refreshToken = :refreshToken AND r.expiresAt > :now")
	Optional<String> findUserIdByRefreshTokenAndNotExpired(@Param("refreshToken") String refreshToken,
														   @Param("now") LocalDateTime now);
}