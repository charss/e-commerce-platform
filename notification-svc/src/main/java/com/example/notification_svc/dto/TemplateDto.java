package com.example.notification_svc.dto;

import com.example.shared.enums.NotificationType;

public record TemplateDto(
        Long id,
        NotificationType type,
        String subject,
        String bodyHtml,
        String bodyText,
        Boolean isActive
) { }
