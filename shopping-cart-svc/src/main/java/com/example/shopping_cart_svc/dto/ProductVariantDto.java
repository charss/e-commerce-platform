package com.example.shopping_cart_svc.dto;

import java.util.UUID;

public record ProductVariantDto(
        Integer id,
        UUID productId,
        String sku,
        Integer quantity
) { }
