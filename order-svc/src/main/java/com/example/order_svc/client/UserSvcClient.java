package com.example.order_svc.client;

import com.example.order_svc.config.UserSvcProperties;
import com.example.shared.dto.UserWithIdDto;
import com.example.shared.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserSvcClient {
    private final WebClient webClient;

    public UserSvcClient(UserSvcProperties props) {
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public UserWithIdDto getUserById(UUID id) {
        return webClient.get()
                .uri("/v1/user/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new UserNotFoundException("Client Error: User with ID " + id + " not found"));
                    }
                    return response.createException();
                })
                .bodyToMono(UserWithIdDto.class)
                .block();
    }
}
