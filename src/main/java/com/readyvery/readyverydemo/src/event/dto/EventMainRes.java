package com.readyvery.readyverydemo.src.event.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventMainRes {
	private List<MainEventDto> mainEvents;
}
