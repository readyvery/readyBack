package com.readyvery.readyverydemo.src.user;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
	UserAuthRes getUserAuthById(CustomUserDetails userDetails);

	UserInfoRes getUserInfoById(Long id);

	void removeRefreshTokenInDB(Long id, HttpServletResponse response);

}
