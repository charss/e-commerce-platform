package com.example.user_svc.client;

import com.example.user_svc.config.ShoppingCartSvcProperties;
import com.example.user_svc.dto.CreateShoppingCartDto;
import com.example.user_svc.dto.ShoppingCartUserDto;
import com.example.user_svc.exception.CreateShoppingCartException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ShoppingCartSvcClient {
    private final WebClient webClient;

    public ShoppingCartSvcClient(ShoppingCartSvcProperties props) {
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public void createShoppingCart(CreateShoppingCartDto createCartDto) {
        webClient.post()
                .uri("/v1/shopping-cart")
                .bodyValue(createCartDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).map(error -> new CreateShoppingCartException("Shopping Cart Service error: " + error))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).map(error -> new CreateShoppingCartException("Server error: " + error))
                )
                .bodyToMono(ShoppingCartUserDto.class)
                .block();
    }
}
