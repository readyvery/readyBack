package com.readyvery.readyverydemo.src.point.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPointHistoryRes {
	private List<PointDto> history;
}
