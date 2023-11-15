package com.readyvery.readyverydemo.src.order;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.Foodie;
import com.readyvery.readyverydemo.domain.repository.FoodieRepository;
import com.readyvery.readyverydemo.global.exception.BusinessLogicException;
import com.readyvery.readyverydemo.global.exception.ExceptionCode;
import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;
import com.readyvery.readyverydemo.src.order.dto.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final FoodieRepository foodieRepository;
	private final OrderMapper orderMapper;

	@Override
	public FoodyDetailRes getFoody(Long storeId, Long foodyId, Long inout) {
		Foodie foodie = foodieRepository.findById(foodyId).orElseThrow(
			() -> new BusinessLogicException(ExceptionCode.FOODY_NOT_FOUND)
		);
		return orderMapper.foodieToFoodyDetailRes(foodie, inout);
	}
}
