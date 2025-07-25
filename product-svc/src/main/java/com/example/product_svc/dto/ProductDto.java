package com.example.product_svc.dto;

import com.example.product_svc.entity.Category;

import java.util.List;
import java.util.UUID;

public record ProductDto (
        UUID uid,
        String name,
        String description,
        List<Category> category,
        List<ProductVariantDto> variants
) { }
