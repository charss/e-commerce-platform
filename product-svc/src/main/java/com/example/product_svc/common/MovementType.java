package com.example.product_svc.common;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MovementType {
    IN,
    OUT,
    RETURN
}
