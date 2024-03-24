package com.readyvery.readyverydemo.src.point.dto;

import static com.readyvery.readyverydemo.global.Constant.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.Point;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointMapper {
	public GetPointRes toGetPointRes(Long point) {
		return GetPointRes.builder().point(point).build();
	}

	public GetPointHistoryRes toGetPointHistoryRes(List<Point> points) {
		return GetPointHistoryRes
			.builder()
			.history(points.stream().filter(point -> !point.getIsDeleted()).map(this::pointToPointDto).toList())
			.build();
	}

	private PointDto pointToPointDto(Point point) {
		return PointDto.builder()
			.point((point.getPoint() >= 0 ? "+" : "") + point.getPoint()) // +1 or -1
			.date(point.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
			.status(point.getIsDeleted())
			.store(point.getOrder().getStore().getName())
			.build();
	}
}
