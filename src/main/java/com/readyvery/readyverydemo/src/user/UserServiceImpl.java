package com.readyvery.readyverydemo.src.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
	@Value("${jwt.refresh.cookie}")
	private String refreshCookie;
	@Value("${service.app.admin.key}")
	private String serviceAppAdminKey;

	@Override
	public UserAuthRes getUserAuthByCustomUserDetails(CustomUserDetails userDetails) {
		verifyUserDetails(userDetails);
		return userMapper.userInfoToUserAuthRes(userDetails);

	}

	private void verifyUserDetails(CustomUserDetails userDetails) {
		if (userDetails == null) {
			throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
		}
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

	@Override
	public UserRemoveRes removeUser(Long id, HttpServletResponse response) throws IOException {
		UserInfo user = getUserInfo(id);
		String kakaoRes = requestToServer("https://kapi.kakao.com/v1/user/unlink", "KakaoAK " + serviceAppAdminKey,
			"target_id_type=user_id&target_id=" + user.getSocialId());
		user.updateRemoveUserDate();
		user.updateRefresh(null); // Refresh Token을 null 또는 빈 문자열로 업데이트
		userRepository.save(user);
		invalidateRefreshTokenCookie(response); // 쿠키 무효화
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

	private String requestToServer(String kakaoApiurl, String headerStr, String postData) throws IOException {
		URL url = new URL(kakaoApiurl);
		HttpURLConnection connectReq = null;

		try {
			connectReq = (HttpURLConnection)url.openConnection();
			connectReq.setRequestMethod("POST");
			connectReq.setDoOutput(true); // Enable writing to the connection output stream

			// Set headers
			connectReq.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if (headerStr != null && !headerStr.isEmpty()) {
				connectReq.setRequestProperty("Authorization", headerStr);
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



