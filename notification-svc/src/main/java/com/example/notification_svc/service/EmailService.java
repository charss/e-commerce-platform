package com.example.notification_svc.service;

import com.example.notification_svc.dto.NotificationResultDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String mailFrom;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.from}") String mailFrom) {
        this.mailSender = mailSender;
        this.mailFrom = mailFrom;
    }

    public NotificationResultDto sendEmailWithHtmlAndText(String to, String subject, String textContent, String htmlContent, Long notifId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textContent, htmlContent);
            message.setFrom(mailFrom);
            mailSender.send(message);

            return new NotificationResultDto(true, "Email sent successfully", notifId);
        } catch (MailException | MessagingException e) {
            String message = "Failed to send email to " + to + ". " + e.getCause().getMessage();
            return new NotificationResultDto(false, message, notifId);
        }
    }
}
