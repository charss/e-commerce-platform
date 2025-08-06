package com.example.notification_svc.controller;


import com.example.notification_svc.dto.TemplateDto;
import com.example.notification_svc.service.NotificationService;
import com.example.notification_svc.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template")
public class TemplateController {
    @Autowired
    NotificationService notifService;
    @Autowired
    TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<TemplateDto>> getAllTemplates() {
        List<TemplateDto> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }
}
