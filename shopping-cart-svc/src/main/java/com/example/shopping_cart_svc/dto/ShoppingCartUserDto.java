package com.example.shopping_cart_svc.dto;

import java.util.UUID;

public record ShoppingCartUserDto(
        Integer shoppingCartId,
        UUID userId
) { }
