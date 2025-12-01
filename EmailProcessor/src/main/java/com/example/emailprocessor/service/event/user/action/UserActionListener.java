package com.example.emailprocessor.service.event.user.action;

import com.example.emailprocessor.dto.SendSimpleEmailEvent;
import com.example.emailprocessor.service.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserActionListener {

    private final EmailService emailService;

    public UserActionListener(EmailService emailService){
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void sendSimpleEmailListener(final SendSimpleEmailEvent event){
        log.info("Sending email to {} with subject {}", event.getTo(), event.getSubject());
        emailService.sendSimpleEmailEvent(event.getTo(), event.getSubject(), event.getText());
    }
}
