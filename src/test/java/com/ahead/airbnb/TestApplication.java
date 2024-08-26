package com.ahead.airbnb;

import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main)
			.with(ContainersConfiguration.class, IngestionConfiguration.class)
			.run(args);
	}

}
