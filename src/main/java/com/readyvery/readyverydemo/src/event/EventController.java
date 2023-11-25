package com.readyvery.readyverydemo.src.event;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.src.event.dto.EventMainRes;
import com.readyvery.readyverydemo.src.event.dto.EventRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
	private final EventService eventService;

	@GetMapping("/banner")
	public ResponseEntity<EventRes> getBanners() {
		EventRes eventRes = eventService.getBanners();
		return new ResponseEntity<>(eventRes, HttpStatus.OK);
	}

	@GetMapping("/main")
	public ResponseEntity<EventMainRes> getMain() {
		EventMainRes eventMainRes = eventService.getMainEvents();
		return new ResponseEntity<>(eventMainRes, HttpStatus.OK);
	}
}
