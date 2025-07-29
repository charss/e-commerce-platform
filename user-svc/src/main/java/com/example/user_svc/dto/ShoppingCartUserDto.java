package com.example.user_svc.dto;

import java.util.UUID;

public record ShoppingCartUserDto(
        Integer shoppingCartId,
        UUID userId
) { }
