package com.example.shared.dto;

import java.util.UUID;

public record UserWithIdDto(
        UUID id,
        String lastName,
        String firstName
) { }
