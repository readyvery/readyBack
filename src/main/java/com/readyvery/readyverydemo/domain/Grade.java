package com.readyvery.readyverydemo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {

	ORDA("ORDA", 4.0D),
	DEFAULT("Earlier", 6.3D);

	private final String description;
	private final Double fee;
}
