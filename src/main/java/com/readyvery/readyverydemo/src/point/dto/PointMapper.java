package com.readyvery.readyverydemo.src.point.dto;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointMapper {
	public GetPointRes toGetPointRes(Long point) {
		return GetPointRes.builder().point(point).build();
	}
}
