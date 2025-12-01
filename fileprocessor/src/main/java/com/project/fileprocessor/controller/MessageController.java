package com.project.fileprocessor.controller;

import com.project.fileprocessor.dto.SendSimpleEmailEvent;
import com.project.fileprocessor.producer.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class MessageController {

    private final RabbitMQProducer rabbitMQProducer;

    public MessageController(final RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") SendSimpleEmailEvent message) {
        rabbitMQProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ...");
    }
}
