package com.example.shared.dto;

import java.util.List;
import java.util.UUID;

public record ProductVariantResponseDto(
        Integer id,
        UUID productId,
        String sku,
        List<ProductVariantAttributeDto> details,
        Integer stock,
        String price
) { }
