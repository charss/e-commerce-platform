package com.example.shopping_cart_svc.dto;

public record CreateCartItemDto(
        Integer shoppingCartId,
        Integer productVariantId,
        Integer quantity
) { }
