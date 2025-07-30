package com.readyvery.readyverydemo.src.refreshtoken;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
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
			log.info("Redis에서 리프레시 토큰 삭제 완료: {}", email);
			// Redis 성공 시 DB에서도 삭제 (있다면)
			fallbackRepository.deleteById(email);
		} catch (Exception e) {
			log.warn("Redis 삭제 실패, DB fallback 사용: {}", e.getMessage());
			// Redis 실패 시에만 DB에서 삭제
			try {
				fallbackRepository.deleteById(email);
				log.info("DB에서 리프레시 토큰 삭제 완료: {}", email);
			} catch (Exception dbException) {
				log.error("DB에서도 토큰 삭제 실패: {}", dbException.getMessage());
			}
		}
	}

	@Override
	public void saveRefreshTokenInRedis(String email, String refreshToken) {
		boolean redisSuccess = false;

		try {
			redisTemplate.opsForValue().set(
				REFRESH_TOKEN_PREFIX + email,
				refreshToken,
				REFRESH_TOKEN_TTL,
				TimeUnit.SECONDS
			);
			redisSuccess = true;
			log.info("Redis에 리프레시 토큰 저장 완료: {}", email);
		} catch (Exception e) {
			log.warn("Redis 저장 실패, DB fallback 사용: {}", e.getMessage());
		}

		if (redisSuccess) {
			// Redis 저장 성공 시 DB에 있는 기존 토큰 삭제 (있다면)
			try {
				fallbackRepository.deleteById(email);
			} catch (Exception e) {
				log.debug("DB 토큰 삭제 중 오류 (무시 가능): {}", e.getMessage());
			}
		} else {
			// Redis 실패 시에만 DB에 저장
			try {
				fallbackRepository.save(
					new RefreshTokenFallback(email, refreshToken, LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TTL))
				);
				log.info("DB에 리프레시 토큰 저장 완료 (fallback): {}", email);
			} catch (Exception dbException) {
				log.error("Redis와 DB 모두에서 토큰 저장 실패: {}", dbException.getMessage());
			}
		}
	}

	@Override
	public String getRefreshToken(String email) {
		try {
			String redisToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + email);
			if (redisToken != null) {
				log.debug("Redis에서 리프레시 토큰 조회 완료: {}", email);
				return redisToken;
			}
			log.debug("Redis에 리프레시 토큰이 없음, DB fallback 사용: {}", email);
		} catch (Exception e) {
			log.warn("Redis 조회 실패, DB fallback 사용: {}", e.getMessage());
		}

		// Redis에 없거나 실패한 경우 DB에서 조회
		try {
			String dbToken = fallbackRepository.findById(email)
				.filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
				.map(RefreshTokenFallback::getRefreshToken)
				.orElse(null);

			if (dbToken != null) {
				log.debug("DB에서 리프레시 토큰 조회 완료 (fallback): {}", email);
			}

			return dbToken;
		} catch (Exception dbException) {
			log.error("DB에서도 토큰 조회 실패: {}", dbException.getMessage());
			return null;
		}
	}

	@Override
	public String findEmailByRefreshToken(String refreshToken) {
		try {
			// Redis에서 모든 refreshToken: 패턴의 키를 검색
			var keys = redisTemplate.keys(REFRESH_TOKEN_PREFIX + "*");
			if (keys != null) {
				for (String key : keys) {
					try {
						String storedToken = redisTemplate.opsForValue().get(key);
						if (refreshToken.equals(storedToken)) {
							String email = key.substring(REFRESH_TOKEN_PREFIX.length());
							log.debug("Redis에서 이메일 조회 완료: {}", email);
							return email;
						}
					} catch (Exception keyException) {
						log.debug("Redis 개별 키 조회 실패: {}", keyException.getMessage());
					}
				}
			}
			log.debug("Redis에서 해당 리프레시 토큰을 찾을 수 없음, DB fallback 사용");
		} catch (Exception e) {
			log.warn("Redis에서 이메일 조회 실패, DB fallback 사용: {}", e.getMessage());
		}

		// Redis에서 찾지 못한 경우 DB에서 효율적으로 조회
		try {
			String email = fallbackRepository.findUserIdByRefreshTokenAndNotExpired(refreshToken, LocalDateTime.now())
				.orElse(null);

			if (email != null) {
				log.debug("DB에서 이메일 조회 완료 (fallback): {}", email);
			}

			return email;
		} catch (Exception dbException) {
			log.error("DB에서도 이메일 조회 실패: {}", dbException.getMessage());
			return null;
		}
	}
}
