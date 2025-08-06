package com.example.notification_svc.controller;


import com.example.notification_svc.dto.NotificationResultDto;
import com.example.notification_svc.service.NotificationService;
import com.example.shared.dto.CreateNotificationDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Autowired
    NotificationService notifService;

    @PostMapping
    public ResponseEntity<NotificationResultDto> processNotification(@Valid @RequestBody CreateNotificationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notifService.processNotification(dto));
    }
}
