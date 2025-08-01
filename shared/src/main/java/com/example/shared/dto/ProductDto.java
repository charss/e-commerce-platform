package com.example.shared.dto;

import java.util.List;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        String description,
//        List<Category> category,
        List<ProductVariantBasicDto> variants
) { }
