package com.example.notification_svc.service;

import com.example.notification_svc.dto.TemplateDto;
import com.example.notification_svc.repository.TemplateRepository;
import com.example.shared.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    @Autowired
    TemplateRepository templateRepo;

    public List<TemplateDto> getAllTemplates() {
        return templateRepo.findAll().stream()
                .map(template -> new TemplateDto(
                        template.getId(),
                        template.getType(),
                        template.getSubject(),
                        template.getBodyHtml(),
                        template.getBodyText(),
                        template.getActive()
                ))
                .toList();
    }
}
