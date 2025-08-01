package com.example.order_svc.util;

import com.example.order_svc.exception.*;
import com.example.shared.exception.UserNotFoundException;
import com.example.shared.exception.response.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(ProductVariantNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleProductVariantNotFound(ProductVariantNotFoundException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(value = InvalidOrderItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleInvalidUpsert(InvalidOrderItemException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleOrderNotFound(OrderNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleInvalidOrderState(InvalidOrderStateException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(ProductClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleProductClientError(ProductClientException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleEnumConversionError(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid value for enum. Please provide a valid order status."
            );
        }

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body."
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new com.example.shared.exception.response.FieldErrorDetail(
                        error.getField(),
                        error.getDefaultMessage()
                )).toList();
        return new com.example.shared.exception.response.ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors);
    }
}
