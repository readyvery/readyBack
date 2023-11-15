package com.readyvery.readyverydemo.src.user;

import com.readyvery.readyverydemo.src.user.dto.UserAuthRes;
import com.readyvery.readyverydemo.src.user.dto.UserInfoRes;

public interface UserService {
	// 기존 메서드들...

	UserAuthRes getUserAuthByEmail(String email);

	UserInfoRes getUserInfoByEmail(String email);

}
