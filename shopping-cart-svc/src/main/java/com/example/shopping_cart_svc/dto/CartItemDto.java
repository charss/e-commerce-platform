package com.example.shopping_cart_svc.dto;

public record CartItemDto(
        Integer id,
        Integer shoppingCartId,
        Integer productId,
        String sku,
        Integer quantity
) { }
