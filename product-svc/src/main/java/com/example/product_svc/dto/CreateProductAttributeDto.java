package com.example.product_svc.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductAttributeDto(
        @NotBlank(message = "Name is required")
        String name
) { }
