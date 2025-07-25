package com.example.product_svc.exception.response;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String message,
        List<FieldErrorDetail> errors
) { }
