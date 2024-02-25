package com.readyvery.readyverydemo.src.point;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.point.dto.GetPointRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PointController {
	private final PointService pointService;

	@GetMapping("/")
	public ResponseEntity<GetPointRes> getPoint(@AuthenticationPrincipal CustomUserDetails userDetails) {
		GetPointRes getPointRes = pointService.getPoint(userDetails);
		return new ResponseEntity<>(getPointRes, HttpStatus.OK);
	}
}
