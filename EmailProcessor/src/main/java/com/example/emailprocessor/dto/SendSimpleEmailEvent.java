package com.example.emailprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendSimpleEmailEvent {

    private String to;

    private String subject;

    private String text;

}
