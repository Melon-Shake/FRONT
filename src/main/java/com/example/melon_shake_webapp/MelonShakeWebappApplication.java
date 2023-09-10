package com.example.melon_shake_webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MelonShakeWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MelonShakeWebappApplication.class, args);
	}

}
