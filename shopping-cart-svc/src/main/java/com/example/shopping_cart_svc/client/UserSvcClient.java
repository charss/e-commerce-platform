package com.example.shopping_cart_svc.client;

import com.example.shopping_cart_svc.config.UserSvcProperties;
import com.example.shopping_cart_svc.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class UserSvcClient {
    private final WebClient webClient;

    public UserSvcClient(UserSvcProperties props) {
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public UserDto getUserById(UUID id) {
        return webClient.get()
                .uri("/v1/user/{id}", id)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }
}
