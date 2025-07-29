package com.example.user_svc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.UUID;

public record CreateShoppingCartDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        UUID userId
) { }
