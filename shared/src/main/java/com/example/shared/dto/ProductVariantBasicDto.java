package com.example.shared.dto;

import java.util.List;
import java.util.UUID;

public record ProductVariantBasicDto(
        Integer id,
        UUID productId,
        String sku,
        List<ProductVariantAttributeDto> details,
        Integer stock,
        Double price
) { }
