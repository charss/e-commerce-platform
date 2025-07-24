package com.example.user_svc.exception.response;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String message,
        List<FieldErrorDetail> errors
) { }
