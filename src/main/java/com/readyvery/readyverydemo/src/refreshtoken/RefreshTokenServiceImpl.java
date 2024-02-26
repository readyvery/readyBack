package com.readyvery.readyverydemo.src.refreshtoken;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.RefreshToken;
import com.readyvery.readyverydemo.domain.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void removeRefreshTokenInRedis(String email) {
		RefreshToken refreshToken = refreshTokenRepository.findById(email)
			.orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
		refreshTokenRepository.delete(refreshToken);
	}

	@Override
	public void saveRefreshTokenInRedis(String email, String refreshToken) {
		RefreshToken token = RefreshToken.builder()
			.id(email)
			.refreshToken(refreshToken)
			.build();
		refreshTokenRepository.save(token);
	}
}
