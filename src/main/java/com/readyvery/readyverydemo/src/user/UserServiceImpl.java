package com.readyvery.readyverydemo.src.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;
import com.readyvery.readyverydemo.src.user.dto.UserMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	@Value("${jwt.refresh.cookie}")
	private String refreshCookie;

	@Override
	public UserAuthRes getUserAuthByCustomUserDetails(CustomUserDetails userDetails) {

		return userMapper.userInfoToUserAuthRes(userDetails);

	}

	@Override
	public UserInfoRes getUserInfoById(Long id) {
		UserInfo userInfo = getUserInfo(id);
		return userMapper.userInfoToUserInfoRes(userInfo);
	}

	@Override
	public void removeRefreshTokenInDB(Long id, HttpServletResponse response) {
		UserInfo user = getUserInfo(id);
		user.updateRefresh(null); // Refresh Token을 null 또는 빈 문자열로 업데이트
		userRepository.save(user);
		invalidateRefreshTokenCookie(response); // 쿠키 무효화
	}

	/**
	 * 로그아웃
	 * @param response
	 */
	private void invalidateRefreshTokenCookie(HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie(refreshCookie, null); // 쿠키 이름을 동일하게 설정
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/api/v1/refresh/token"); // 기존과 동일한 경로 설정
		refreshTokenCookie.setMaxAge(0); // 만료 시간을 0으로 설정하여 즉시 만료
		response.addCookie(refreshTokenCookie);
	}

	private UserInfo getUserInfo(Long id) {
		return userRepository.findById(id).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
		);
	}
}



