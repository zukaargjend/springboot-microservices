package com.example.emailprocessor.controller;

import com.example.emailprocessor.service.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class SendEmailController {

    private final EmailService emailService;

    public SendEmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping("/email/process")
    public void onImportingFileProcessor(
            @RequestParam(defaultValue = "qendrimzeneli195@gmail.com,jdev.fer@gmail.com,gjendi1999@live.com," +
                    "argjendzuka@digitizeict.com") List<String> to) {
        String subject = "Update on Your FileProcessor Status";
        String content = """
                Hello Sir/Madam,

                Your file is processed.

                This is an automated message — please do not reply to this email.

                Best regards,
                Zuka Team
                """;
        for (String recipient : to) {
            log.info("Sending email to: {} subject {}, message body: {}", recipient, subject, content);
            emailService.sendSimpleEmailEvent(recipient, subject, content);
        }
    }
}
