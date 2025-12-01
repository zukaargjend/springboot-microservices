package com.example.emailprocessor.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {

    @Value("${file-processor.sender_email}")
    private String emailSender;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleEmailEvent(String to, String subject, String content){
        final SimpleMailMessage message = new SimpleMailMessage();

        String[] emails = Arrays.stream(to.split(","))
                        .map(String::trim)
                        .toArray(String[]::new);

        message.setFrom(emailSender);
        message.setTo(emails);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }
}
