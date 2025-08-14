package com.example.product_svc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateProductVariantDto(
        @NotNull(message = "Product ID is required")
        UUID productId,
        List<VariantAttributeDto> attributes,
        @NotBlank(message = "SKU is required")
        String sku,
        @NotNull(message = "Unit Price is required")
        Long unitPrice,
        @NotBlank(message = "Currency is required")
        String currency
) { }

