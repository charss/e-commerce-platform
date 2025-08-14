package com.example.payment_svc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PaymentSvcApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+0:00"));
		SpringApplication.run(PaymentSvcApplication.class, args);
	}

}
