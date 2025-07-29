package com.example.user_svc.dto;

import java.util.UUID;

public record UserWithIdDto(
        UUID id,
        String lastName,
        String firstName
) { }
