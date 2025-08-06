package com.example.notification_svc.dto;

public record NotificationResultDto(
        Boolean success,
        String message,
        Long notificationId
) { }
