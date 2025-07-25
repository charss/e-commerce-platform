package com.example.product_svc.exception.response;

public record FieldErrorDetail(
        String field,
        String error
) { }
