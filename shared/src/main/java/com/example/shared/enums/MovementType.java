package com.example.shared.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MovementType {
    IN,
    OUT,
    RESERVE,
    RETURN;

    public Boolean isDeduct() {
        return this == OUT || this == RESERVE;
    }
}
