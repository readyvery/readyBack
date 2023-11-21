package com.readyvery.readyverydemo.security.exception;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readyvery.readyverydemo.security.exception.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Autowired
	public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ErrorResponse errorResponse = new ErrorResponse("Access Denied", request.getRequestURI());
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}

