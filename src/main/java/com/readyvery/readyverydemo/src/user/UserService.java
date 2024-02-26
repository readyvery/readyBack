package com.readyvery.readyverydemo.src.user;

import java.io.IOException;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;
import com.readyvery.readyverydemo.src.user.dto.UserRemoveRes;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
	UserAuthRes getUserAuthByCustomUserDetails(CustomUserDetails userDetails);

	UserInfoRes getUserInfoById(Long id);

	void removeRefreshTokenInDB(String email, HttpServletResponse response);

	UserRemoveRes removeUser(Long id, HttpServletResponse response) throws IOException;

}
