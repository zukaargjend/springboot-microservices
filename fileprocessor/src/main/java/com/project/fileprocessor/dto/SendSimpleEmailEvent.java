package com.project.fileprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendSimpleEmailEvent {

    private String to;

    private String subject;

    private String text;

}
