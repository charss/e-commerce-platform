package com.example.order_svc.client;

import com.example.order_svc.config.ProductSvcProperties;
import com.example.order_svc.exception.ProductClientException;
import com.example.order_svc.exception.ProductVariantNotFoundException;
import com.example.shared.dto.BulkCreateInventoryMovementDto;
import com.example.shared.dto.ProductVariantBasicDto;
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

    public ProductVariantBasicDto getProductVariantById(Integer id) {
        return webClient.get()
                .uri("/v1/product/variant/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ProductVariantNotFoundException("Client Error: Product Variant with ID " + id + " not found."));
                    }
                    return response.createException();
                })
                .bodyToMono(ProductVariantBasicDto.class)
                .block();
    }

    public void processBulkStockMovement(BulkCreateInventoryMovementDto dto) {
        webClient.post()
                .uri("/v1/inventory/bulk") // your inventory service endpoint
                .bodyValue(dto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(error -> Mono.error(new ProductClientException("Client error: " + error))))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(error -> Mono.error(new ProductClientException("Server error: " + error))))
                .toBodilessEntity()
                .block();
    }
}
