package com.readyvery.readyverydemo.src.event.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.readyvery.readyverydemo.domain.CouponDetail;
import com.readyvery.readyverydemo.domain.Event;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventMapper {
	public EventRes toEventRes(List<CouponDetail> couponDetails) {
		return EventRes.builder()
			.banners(couponDetailsToBannerDto(couponDetails))
			.build();
	}

	private List<BannerDto> couponDetailsToBannerDto(List<CouponDetail> couponDetails) {
		return couponDetails.stream()
			.filter(CouponDetail::isActive)
			.map(couponDetail -> BannerDto.builder()
				.idx(couponDetail.getId())
				.bannerImg(couponDetail.getBannerImg())
				.couponCode(couponDetail.getCouponCode())
				.build()
			).toList();
	}

	public EventMainRes toEventMainRes(List<Event> events) {
		return EventMainRes.builder()
			.mainEvents(events.stream().filter(Event::isActive).map(event -> MainEventDto.builder()
				.imgUrl(event.getMainImg())
				.redirectUrl(event.getRedirectUrl())
				.build()
			).toList())
			.build();
	}
}
