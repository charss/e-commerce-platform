package com.example.shopping_cart_svc.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String lastName,
        String firstName
) { }
