package com.readyvery.readyverydemo.src.point;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.point.dto.GetPointRes;

public interface PointService {
	GetPointRes getPoint(CustomUserDetails userDetails);
}
