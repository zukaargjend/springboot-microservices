package com.example.emailprocessor.service.event;

import com.example.emailprocessor.dto.SendSimpleEmailEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher publisher;

    public EventPublisher(ApplicationEventPublisher publisher){
        this.publisher = publisher;
    }

    public void sendSimpleEmail(SendSimpleEmailEvent sendSimpleEmailEvent){
        publisher.publishEvent(sendSimpleEmailEvent);
    }
}
