package com.example.shopping_cart_svc.client;

import com.example.shopping_cart_svc.config.ProductSvcProperties;
import com.example.shopping_cart_svc.dto.ProductVariantDto;
import com.example.shopping_cart_svc.exception.ProductVariantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductSvcClient {
    private final WebClient webClient;

    public ProductSvcClient(ProductSvcProperties props) {
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public ProductVariantDto getProductVariantById(Integer id) {
        return webClient.get()
                .uri("/v1/product/variant/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ProductVariantNotFoundException("Client Error: Product Variant with ID " + id + " not found."));
                    }
                    return response.createException();
                })
                .bodyToMono(ProductVariantDto.class)
                .block();
    }
}
