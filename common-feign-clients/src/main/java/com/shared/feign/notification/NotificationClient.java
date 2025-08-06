package com.shared.feign.notification;

import com.example.shared.dto.CreateNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-svc", url = "${services.notification.base-url}")
public interface NotificationClient {
    @PostMapping("v1/notification")
    void sendEmail(@RequestBody CreateNotificationDto dto);
}
