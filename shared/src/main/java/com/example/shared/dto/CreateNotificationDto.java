package com.example.shared.dto;

import com.example.shared.enums.NotificationType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record CreateNotificationDto(
  NotificationType type,
  UUID userId,
  JsonNode data
) {}
