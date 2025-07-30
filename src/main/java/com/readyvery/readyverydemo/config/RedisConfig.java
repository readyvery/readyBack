package com.readyvery.readyverydemo.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
public class RedisConfig implements CachingConfigurer {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// Redis Standalone 구성 설정
		org.springframework.data.redis.connection.RedisStandaloneConfiguration redisConfig =
			new org.springframework.data.redis.connection.RedisStandaloneConfiguration();
		redisConfig.setHostName(host);
		redisConfig.setPort(port);

		// Redis 연결 실패 시에도 애플리케이션이 계속 동작하도록 타임아웃 설정
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
			.commandTimeout(Duration.ofSeconds(2))  // 짧은 타임아웃으로 빠른 실패
			.shutdownTimeout(Duration.ofMillis(100))
			.build();

		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);

		// 연결 검증 비활성화로 Redis 다운 시에도 애플리케이션 시작 가능
		factory.setValidateConnection(false);
		factory.setShareNativeConnection(false);

		log.info("Redis 연결팩토리 설정 완료: {}:{}", host, port);
		return factory;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// String 직렬화 사용
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(stringSerializer);

		// 연결 실패 시 예외를 던지지 않도록 설정
		template.setEnableDefaultSerializer(false);
		template.setDefaultSerializer(stringSerializer);

		log.info("RedisTemplate 설정 완료 - Redis 다운 시 DB fallback 사용");
		return template;
	}

	/**
	 * Redis 오류 발생 시 graceful 처리를 위한 CacheErrorHandler
	 * Redis가 다운되어도 애플리케이션이 계속 동작하도록 함
	 */
	@Override
	public CacheErrorHandler errorHandler() {
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
				log.warn("Redis 캐시 조회 실패 ({}): {} - DB에서 조회합니다", cache.getName(), exception.getMessage());
			}

			@Override
			public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
				log.warn("Redis 캐시 저장 실패 ({}): {} - 캐시 없이 계속 진행합니다", cache.getName(), exception.getMessage());
			}

			@Override
			public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
				log.warn("Redis 캐시 삭제 실패 ({}): {} - 캐시 없이 계속 진행합니다", cache.getName(), exception.getMessage());
			}

			@Override
			public void handleCacheClearError(RuntimeException exception, Cache cache) {
				log.warn("Redis 캐시 클리어 실패 ({}): {} - 캐시 없이 계속 진행합니다", cache.getName(), exception.getMessage());
			}
		};
	}
}
