package com.example.shopping_cart_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ShoppingCartSvcApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+0:00"));
		SpringApplication.run(ShoppingCartSvcApplication.class, args);
	}

}
