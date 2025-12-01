package com.example.emailprocessor.consumer;

import com.example.emailprocessor.dto.SendSimpleEmailEvent;
import com.example.emailprocessor.service.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationConsumer {

    private final EmailService emailService;

    public EmailNotificationConsumer(final EmailService emailService){
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.email.queue}")
    public void consume(final SendSimpleEmailEvent message) {
        if (message.getTo() == null || message.getTo().isBlank()) {
            log.error("Invalid email address received: {}", message.getTo());
            return;
        }
        emailService.sendSimpleEmailEvent(message.getTo(), message.getSubject(), message.getText());
    }
}
