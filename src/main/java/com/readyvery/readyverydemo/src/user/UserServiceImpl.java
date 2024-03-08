package com.readyvery.readyverydemo.src.user;

import static com.readyvery.readyverydemo.config.JwtConfig.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.readyvery.readyverydemo.config.UserApiConfig;
import com.readyvery.readyverydemo.domain.SocialType;
import com.readyvery.readyverydemo.domain.UserInfo;
import com.readyvery.readyverydemo.domain.repository.UserRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.refreshtoken.RefreshTokenService;
import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;
import com.readyvery.readyverydemo.src.user.dto.UserMapper;
import com.readyvery.readyverydemo.src.user.dto.UserRemoveRes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserApiConfig userApiConfig;
	private final RefreshTokenService refreshTokenServiceImpl;
	private final UserServiceFacade userServiceFacade;

	@Override
	public UserAuthRes getUserAuthByCustomUserDetails(CustomUserDetails userDetails) {
		verifyUserDetails(userDetails);
		UserInfo userInfo = userServiceFacade.getUserInfo(userDetails.getId());
		return userMapper.userInfoToUserAuthRes(userDetails.isEnabled(), userInfo);

	}

	private void verifyUserDetails(CustomUserDetails userDetails) {
		if (userDetails == null) {
			throw new BusinessLogicException(ExceptionCode.AUTH_ERROR);
		}
	}

	@Override
	public UserInfoRes getUserInfoById(Long id) {
		UserInfo userInfo = userServiceFacade.getUserInfo(id);
		return userMapper.userInfoToUserInfoRes(userInfo);
	}

	@Override
	public void removeRefreshTokenInDB(String email, HttpServletResponse response) {
		refreshTokenServiceImpl.removeRefreshTokenInRedis(email); // Redis에서 Refresh Token 삭제
		invalidateRefreshTokenCookie(response); // 쿠키 무효화
	}

	@Override
	public UserRemoveRes removeUser(Long id, HttpServletResponse response) throws IOException {
		UserInfo user = userServiceFacade.getUserInfo(id);
		if (user.getSocialType().equals(SocialType.KAKAO)) {
			requestToServer(
				"KakaoAK " + userApiConfig.getServiceAppAdminKey(),
				"target_id_type=user_id&target_id=" + user.getSocialId());
		}

		removeRefreshTokenInDB(user.getEmail(), response); // Refresh Token 삭제
		user.updateRemoveUserDate(); // 회원 탈퇴 날짜 업데이트
		userRepository.save(user);
		return UserRemoveRes.builder()
			.message("회원 탈퇴가 완료되었습니다.")
			.success(true)
			.build();
	}

	/**
	 * 로그아웃
	 * @param response
	 */
	private void invalidateRefreshTokenCookie(HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie(userApiConfig.getRefreshCookie(), null); // 쿠키 이름을 동일하게 설정
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/api/v1/refresh/token"); // 기존과 동일한 경로 설정
		refreshTokenCookie.setMaxAge(0); // 만료 시간을 0으로 설정하여 즉시 만료
		response.addCookie(refreshTokenCookie);

	}

	private String requestToServer(String headerStr, String postData) throws IOException {
		URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
		HttpURLConnection connectReq = null;

		try {
			connectReq = (HttpURLConnection)url.openConnection();
			connectReq.setRequestMethod("POST");
			connectReq.setDoOutput(true); // Enable writing to the connection output stream

			// Set headers
			connectReq.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if (headerStr != null && !headerStr.isEmpty()) {
				connectReq.setRequestProperty(AUTHORIZATION, headerStr);
			}

			// Write the post data to the request body
			try (OutputStream os = connectReq.getOutputStream()) {
				byte[] input = postData.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int responseCode = connectReq.getResponseCode();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				responseCode == 200 ? connectReq.getInputStream() : connectReq.getErrorStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();
				log.info("responseCode: {}", responseCode);
				while ((inputLine = br.readLine()) != null) {
					response.append(inputLine);
				}
				return responseCode == 200 ? response.toString().replaceAll("&#39;", "") : null;
			}
		} finally {
			if (connectReq != null) {
				connectReq.disconnect();
			}
		}
	}

}



