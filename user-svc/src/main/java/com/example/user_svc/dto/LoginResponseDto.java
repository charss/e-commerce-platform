package com.example.user_svc.dto;

import java.util.UUID;

public record LoginResponseDto (
        String token,
        UUID uid
) { }
