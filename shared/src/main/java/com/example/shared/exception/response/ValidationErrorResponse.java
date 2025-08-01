package com.example.shared.exception.response;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String message,
        List<FieldErrorDetail> errors
) { }
