package com.readyvery.readyverydemo.src.event;

import java.util.List;

import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.domain.CouponDetail;
import com.readyvery.readyverydemo.domain.Event;
import com.readyvery.readyverydemo.domain.repository.CouponDetailRepository;
import com.readyvery.readyverydemo.domain.repository.EventRepository;
import com.readyvery.readyverydemo.src.event.dto.EventMainRes;
import com.readyvery.readyverydemo.src.event.dto.EventMapper;
import com.readyvery.readyverydemo.src.event.dto.EventRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventMapper eventMapper;
	private final CouponDetailRepository couponDetailRepository;
	private final EventRepository eventRepository;

	@Override
	public EventRes getBanners() {
		List<CouponDetail> couponDetails = couponDetailRepository.findAll();
		return eventMapper.toEventRes(couponDetails);
	}

	@Override
	public EventMainRes getMainEvents() {
		List<Event> events = getEvents();
		return eventMapper.toEventMainRes(events);
	}

	private List<Event> getEvents() {
		return eventRepository.findAll();
	}
}
