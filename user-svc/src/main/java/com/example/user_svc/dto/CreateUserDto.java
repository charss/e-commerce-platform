package com.example.user_svc.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDto (
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "First Name is required")
        String firstName,
        @NotBlank(message = "Last Name is required")
        String lastName
) { }
