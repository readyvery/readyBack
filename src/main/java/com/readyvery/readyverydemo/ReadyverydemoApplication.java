package com.readyvery.readyverydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReadyverydemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadyverydemoApplication.class, args);
	}

}
