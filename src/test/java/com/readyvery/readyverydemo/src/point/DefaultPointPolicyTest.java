package com.readyvery.readyverydemo.src.point;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultPointPolicyTest {
	@InjectMocks
	private DefaultPointPolicy defaultPointPolicy;

	@Test
	@DisplayName("작은 금액 테스트")
	void calculateSmallPoint() {
		// given
		Long amount = 1_050L; // 계산 금액
		// when
		Long point = defaultPointPolicy.calculatePoint(amount);
		// then
		assertEquals(26L, point);
	}

	@Test
	@DisplayName("보통 금액 테스트")
	void calculateNomalPoint() {
		// given
		Long amount = 10_000L; // 계산 금액
		// when
		Long point = defaultPointPolicy.calculatePoint(amount);
		// then
		assertEquals(250L, point);
	}

	@Test
	@DisplayName("큰 금액 테스트")
	void calculateBigPoint() {
		// given
		Long amount = 532_400L; // 계산 금액
		// when
		Long point = defaultPointPolicy.calculatePoint(amount);
		// then
		assertEquals(13_310L, point);
	}
}
