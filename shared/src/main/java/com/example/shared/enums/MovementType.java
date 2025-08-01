package com.example.shared.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MovementType {
    IN,
    OUT,
    RESERVE,
    ORDER_CANCELLED,
    RETURN;

    public Boolean isDeduct() {
        return this == RESERVE;
    }

    public Boolean isAdd() {
        return this == IN || this == ORDER_CANCELLED || this == RETURN;
    }
}
