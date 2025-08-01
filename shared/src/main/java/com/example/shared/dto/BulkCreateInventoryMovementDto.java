package com.example.shared.dto;

import java.util.List;

public record BulkCreateInventoryMovementDto(
  List<CreateInventoryMovementDto> movements
) {}
