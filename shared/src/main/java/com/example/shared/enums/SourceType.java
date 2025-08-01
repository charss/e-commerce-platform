package com.example.shared.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SourceType {
    ORDER,
    RETURN,
    MANUAL,
    RESTOCK;
}
