package com.explore.securityApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class securityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(securityAppApplication.class, args);
	}

}
