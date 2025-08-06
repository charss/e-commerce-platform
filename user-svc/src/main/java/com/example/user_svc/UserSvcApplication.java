package com.example.user_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.shared.feign")
public class UserSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserSvcApplication.class, args);
	}

}
