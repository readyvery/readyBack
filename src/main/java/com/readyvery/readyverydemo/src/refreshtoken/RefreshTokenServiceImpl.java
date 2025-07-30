package com.readyvery.readyverydemo.src.refreshtoken;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.RefreshToken;
import com.readyvery.readyverydemo.domain.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import com.readyvery.readyverydemo.src.refreshtoken.fallback.RefreshTokenFallback;
import com.readyvery.readyverydemo.src.refreshtoken.fallback.RefreshTokenFallbackRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RedisTemplate<String, String> redisTemplate;
	private final RefreshTokenFallbackRepository fallbackRepository;
	private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
	private static final long REFRESH_TOKEN_TTL = 60L * 60 * 24 * 7; 

	@Override
	public void removeRefreshTokenInRedis(String email) {
		try {
			redisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
		} catch (Exception e) {
			log.error("Redis 삭제 실패, DB fallback 사용: {}", e.getMessage());
			fallbackRepository.deleteById(email);
		}
	}

	@Override
	public void saveRefreshTokenInRedis(String email, String refreshToken) {
		try {
			redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + email, refreshToken, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Redis 저장 실패, DB fallback 사용: {}", e.getMessage());
			fallbackRepository.save(
				new RefreshTokenFallback(email, refreshToken, LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TTL))
			);
		}
	}

	public String getRefreshToken(String email) {
		try {
			return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + email);
		} catch (Exception e) {
			log.error("Redis 조회 실패, DB fallback 사용: {}", e.getMessage());
			return fallbackRepository.findById(email)
				.filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
				.map(RefreshTokenFallback::getRefreshToken)
				.orElse(null);
		}
	}
}
