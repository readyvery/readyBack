package com.readyvery.readyverydemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		title = "ReadyVery 서비스 API 명세서",
		description = "☕ [유저사이드] 간편 선결제 테이크아웃 서비스, 레디베리 \uD83E\uDD64",
		version = "v1"
	)
)
@Configuration
public class SwaggerConfig {

	@Value("${production.server.url}")
	private String serverUrl;

	@Bean
	public OpenAPI customOpenApi() {

		return new OpenAPI()
			.addServersItem(new Server().url(serverUrl).description("Production server"))
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
			.components(new Components()
				.addSecuritySchemes("bearerAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization")));

	}
}
