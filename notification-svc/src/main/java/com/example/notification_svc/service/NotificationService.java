package com.example.notification_svc.service;

import com.example.notification_svc.dto.NotificationResultDto;
import com.example.notification_svc.dto.OrderItemBasicDto;
import com.example.notification_svc.dto.data.OrderConfirmedData;
import com.example.notification_svc.dto.data.UserRegisteredData;
import com.example.notification_svc.entity.Notification;
import com.example.notification_svc.entity.Template;
import com.example.notification_svc.exception.InvalidNotificationTypeException;
import com.example.notification_svc.exception.TemplateNotFoundException;
import com.example.notification_svc.repository.NotificationRepository;
import com.example.notification_svc.repository.TemplateRepository;
import com.example.shared.dto.CreateNotificationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notifRepo;
    @Autowired
    TemplateRepository templateRepo;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EmailService emailService;

    public NotificationResultDto processNotification(CreateNotificationDto dto) {
        return switch (dto.type()) {
            case USER_REGISTERED -> processUserRegistered(dto);
            case ORDER_CONFIRMED -> processOrderConfirmed(dto);
            default -> throw new InvalidNotificationTypeException("Unsupported notification type: " + dto.type());
        };
    }

    @Transactional
    private NotificationResultDto processUserRegistered(CreateNotificationDto dto) {
        Template template = templateRepo.findByType(dto.type()).orElseThrow(
                () -> new TemplateNotFoundException("Template with type " + dto.type() + " not found."));
        UserRegisteredData notifData = convertData(dto.data(), UserRegisteredData.class);

        Notification notification = buildNotification(dto, template);

        String subject = template.getSubject().replace("{{firstName}}", notifData.firstName());
        String emailHtml = template.getBodyHtml().replace("{{firstName}}", notifData.firstName());
        String emailText = template.getBodyText().replace("{{firstName}}", notifData.firstName());

        NotificationResultDto resultDto = emailService.sendEmailWithHtmlAndText(notifData.email(), subject, emailText, emailHtml, notification.getId());
        updateNotificationStatus(notification, resultDto);
        return resultDto;
    }

    @Transactional
    private NotificationResultDto processOrderConfirmed(CreateNotificationDto dto) {
        Template template = templateRepo.findByType(dto.type()).orElseThrow(
                () -> new TemplateNotFoundException("Template with type " + dto.type() + " not found."));
        OrderConfirmedData notifData = convertData(dto.data(), OrderConfirmedData.class);
        Notification notification = buildNotification(dto, template);

        String subject = template.getSubject().replace("{{orderId}}", notifData.orderId());
        StringBuilder listString = new StringBuilder();
        StringBuilder plainText = new StringBuilder();
        for (OrderItemBasicDto item : notifData.items()) {
            listString.append(String.format(
                    "<li>%s (Quantity: %s) - $%s</li>",
                    item.sku(),
                    item.quantity(),
                    item.pricePerUnit()
            ));
            plainText.append(String.format(
                    "%s (Quantity: %s) - $%s\n",
                    item.sku(),
                    item.quantity(),
                    item.pricePerUnit()
            ));
        }

        String emailHtml = template.getBodyHtml()
                .replace("{{firstName}}", notifData.firstName())
                .replace("{{orderId}}", notifData.orderId())
                .replace("{{orderDate}}", notifData.orderDate())
                .replace("{{items}}", listString.toString())
                .replace("{{total}}", notifData.total());

        String emailText = template.getBodyText()
                .replace("{{firstName}}", notifData.firstName())
                .replace("{{orderId}}", notifData.orderId())
                .replace("{{orderDate}}", notifData.orderDate())
                .replace("{{items}}", plainText.toString())
                .replace("{{total}}", notifData.total());

        NotificationResultDto resultDto = emailService.sendEmailWithHtmlAndText(notifData.email(), subject, emailText, emailHtml, notification.getId());
        updateNotificationStatus(notification, resultDto);
        return resultDto;
    }

    private Notification buildNotification(CreateNotificationDto createDto, Template template) {
        Map<String, Object> map = objectMapper.convertValue(createDto.data(), new TypeReference<>() {});
        Notification notif = new Notification();
        notif.setUserId(createDto.userId());
        notif.setType(createDto.type());
        notif.setStatus("pending");
        notif.setData(map);
        notif.setTemplate(template);
        return notifRepo.saveAndFlush(notif);
    }

    private void updateNotificationStatus(Notification notification, NotificationResultDto resultDto) {
        notification.setStatus(resultDto.success() ? "sent" : "failed");
        notification.setSentAt(resultDto.success() ? OffsetDateTime.now() : null);
        notifRepo.save(notification);
    }

    private <T> T convertData(JsonNode data, Class<T> tClass) {
        try {
            return objectMapper.treeToValue(data, tClass);
        } catch (JsonProcessingException e) {
            throw new InvalidNotificationTypeException("Deserialization failed for " + tClass.getSimpleName());
        }
    }
}

