package com.example.notification_svc.repository;

import com.example.notification_svc.entity.Template;
import com.example.shared.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByType(NotificationType type);

    // Custom JPQL query
    @Query(
            value = "SELECT id, type, subject, body_html, body_text, is_active, created_at FROM notification.template WHERE type = :type AND is_active = true",
            nativeQuery = true
    )
    List<Template> findActiveTemplatesByType(@Param("type") String type);
}
