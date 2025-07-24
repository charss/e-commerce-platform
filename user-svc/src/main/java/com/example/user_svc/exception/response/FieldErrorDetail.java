package com.example.user_svc.exception.response;

public record FieldErrorDetail(
        String field,
        String error
) { }
