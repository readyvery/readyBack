package com.readyvery.readyverydemo.src.point;

import org.springframework.stereotype.Service;

@Service
public class DefaultPointPolicy implements PointPolicy {
	private Double pointRate = 2.5;

	@Override
	public Long calculatePoint(Long price) {
		Double tmp = price * pointRate / 100 + 0.5; // 반올림
		return tmp.longValue();
	}
}
