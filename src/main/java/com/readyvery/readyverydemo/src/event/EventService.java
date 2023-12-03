package com.readyvery.readyverydemo.src.event;

import com.readyvery.readyverydemo.src.event.dto.EventMainRes;
import com.readyvery.readyverydemo.src.event.dto.EventRes;

public interface EventService {
	EventRes getBanners();

	EventMainRes getMainEvents();
}
