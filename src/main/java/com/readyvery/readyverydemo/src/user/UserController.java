package com.readyvery.readyverydemo.src.user;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;
import com.readyvery.readyverydemo.src.user.dto.UserLogoutRes;
import com.readyvery.readyverydemo.src.user.dto.UserRemoveRes;

import jakarta.servlet.http.HttpServletResponse;
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
	 * 사용자 인증 체크
	 * 인증체크 후 사용자 정보를 반환합니다.
	 * DB의 조회 없이 반환
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/auth")
	public UserAuthRes userAuth(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return userServiceImpl.getUserAuthByCustomUserDetails(userDetails);
	}

	/**
	 * 사용자 정보 조회
	 */
	@GetMapping("/user/info")
	public UserInfoRes userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return userServiceImpl.getUserInfoById(userDetails.getId());
	}

	/**
	 * 사용자 정보 조회
	 * CustomUserDetails의 내부 구현체인 UserDetails를 사용하여도 사용자 정보를 조회가능
	 * 인증체크 후 사용자 정보를 반환
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/user/detail/info")
	public CustomUserDetails userDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return userDetails;
	}

	/**
	 * 사용자 로그아웃
	 */
	@GetMapping("/user/logout")
	public UserLogoutRes logout(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) {

		userServiceImpl.removeRefreshTokenInDB(userDetails.getId(), response);
		return UserLogoutRes.builder()
			.success(true)
			.message("로그아웃 성공")
			.build();
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

	/**
	 * 회원 탈퇴
	 * @param userDetails
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/user/remove")
	public UserRemoveRes remove(@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletResponse response) throws IOException {
		return userServiceImpl.removeUser(userDetails.getId(), response);
	}

}
