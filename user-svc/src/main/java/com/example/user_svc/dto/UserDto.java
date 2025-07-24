package com.example.user_svc.dto;

import java.util.UUID;

public record UserDto (
        String username,
        String lastName,
        String firstName
) { }
