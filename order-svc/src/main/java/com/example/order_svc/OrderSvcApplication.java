package com.example.order_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.shared.feign")
public class OrderSvcApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+0:00"));
		SpringApplication.run(OrderSvcApplication.class, args);
	}

}
