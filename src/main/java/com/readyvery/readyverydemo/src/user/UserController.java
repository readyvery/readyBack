package com.readyvery.readyverydemo.src.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userServiceImpl;

	@GetMapping("/jwt-test")
	public String jwtTest() {
		return "jwtTest 요청 성공";
	}

	/**
	 * 사용자 정보 조회
	 * 인증체크 후 사용자 정보를 반환합니다.
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/auth")
	public UserAuthRes userAuth(@AuthenticationPrincipal UserDetails userDetails) {
		// 서비스 계층을 호출하여 사용자 정보를 조회합니다.
		return userServiceImpl.getUserAuthByEmail(userDetails.getUsername());
	}

	/**
	 *
	 */
	@GetMapping("/user/info")
	public UserInfoRes userInfo(@AuthenticationPrincipal UserDetails userDetails) {
		return userServiceImpl.getUserInfoByEmail(userDetails.getUsername());
	}

	/**
	 * Access 토큰 재발급
	 *
	 * @return
	 */
	@GetMapping("/refresh/token")
	public boolean refreshEndpoint() {
		return true;
	}

}
