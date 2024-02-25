package com.readyvery.readyverydemo.src.point;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PointController {
	private final PointService pointService;
}
