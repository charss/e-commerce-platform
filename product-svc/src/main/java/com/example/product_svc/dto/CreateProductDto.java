package com.example.product_svc.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductDto(
        @NotBlank(message = "Name is required")
        String name,
        String description
) { }
