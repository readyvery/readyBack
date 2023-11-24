package com.readyvery.readyverydemo.src.event;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.src.event.dto.EventRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
	private final EventService eventService;

	@GetMapping("/banner")
	public EventRes getBanners() {
		return eventService.getBanners();
	}
}
